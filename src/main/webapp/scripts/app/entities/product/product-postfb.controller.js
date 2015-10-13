'use strict';

angular.module('coreApp').controller('ProductPostfbController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Product', 'ChannelPost', 'ChannelPostsByProductIds', 'MerchantChannelByChannel',
        function($scope, $stateParams, $modalInstance, entity, Product, ChannelPost, ChannelPostsByProductIds, MerchantChannelByChannel) {

            $scope.product = entity;

            $scope.load = function(id) {
                Product.get({id : id}, function(result) {
                    $scope.product = result;
                    $scope.loadChannelPost();
                    $scope.loadMerchantChannelId();
                });
            };
            $scope.loadChannelPost = function() {
                $scope.productIds = "";
                if($scope.product) {
                    $scope.productIds = $scope.product.id;
                }
                console.log("postfb productIds = " + $scope.productIds);
                ChannelPostsByProductIds.get({productIds: $scope.productIds}, function(result) {
                    $scope.channelPost = result[$scope.product.id];
                });
            };

            $scope.loadMerchantChannelId = function() {
                MerchantChannelByChannel.get({channel:'facebook'}, function(result) {
                    $scope.merchantChannelId = result.id;
                    console.log("merchant channel id = " + $scope.merchantChannelId);
                });
            };

            $scope.load($stateParams.id);

            $scope.getStatus = function(productId) {
                //console.log("productId = " + productId);
                if($scope.channelPost) {
                    //console.log("status = " + channelPost.status);
                    if($scope.channelPost.status == 'Pending' || $scope.channelPost.status == 'Published' || $scope.channelPost.status == 'Error')
                        return $scope.channelPost.status;
                }
                //console.log("status = none");
                return "none";
            };

            var onSaveFinished = function (result) {
                console.log("onSaveFinished 1 => " + result);
                $scope.$emit('coreApp:channelPostUpdate', result);
                console.log("onSaveFinished 1 => " + result);
                $modalInstance.close(result);
            };

            $scope.save = function () {
                $scope.channelPost = {product:{id:$scope.product.id}, merchantChannel:{id:$scope.merchantChannelId}}
                console.log("save channel post => " + JSON.stringify($scope.channelPost));
                ChannelPost.save($scope.channelPost, onSaveFinished);
                $modalInstance.dismiss('cancel');
            };

            $scope.clear = function() {
                $modalInstance.dismiss('cancel');
            };

            $scope.abbreviate = function (text) {
                if (!angular.isString(text)) {
                    return '';
                }
                if (text.length < 30) {
                    return text;
                }
                return text ? (text.substring(0, 15) + '...' + text.slice(-10)) : '';
            };

            $scope.byteSize = function (base64String) {
                if (!angular.isString(base64String)) {
                    return '';
                }
                function endsWith(suffix, str) {
                    return str.indexOf(suffix, str.length - suffix.length) !== -1;
                }
                function paddingSize(base64String) {
                    if (endsWith('==', base64String)) {
                        return 2;
                    }
                    if (endsWith('=', base64String)) {
                        return 1;
                    }
                    return 0;
                }
                function size(base64String) {
                    return base64String.length / 4 * 3 - paddingSize(base64String);
                }
                function formatAsBytes(size) {
                    return size.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ") + " bytes";
                }

                return formatAsBytes(size(base64String));
            };

            $scope.setMainImage = function ($files, product) {
                if ($files[0]) {
                    var file = $files[0];
                    var fileReader = new FileReader();
                    fileReader.readAsDataURL(file);
                    fileReader.onload = function (e) {
                        var data = e.target.result;
                        var base64Data = data.substr(data.indexOf('base64,') + 'base64,'.length);
                        $scope.$apply(function() {
                            product.mainImage = base64Data;
                        });
                    };
                }
            };
        }]);
