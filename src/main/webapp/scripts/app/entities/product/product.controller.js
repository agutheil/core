'use strict';

angular.module('coreApp')
    .controller('ProductController', function ($scope, Product, ParseLinks, ChannelPostsByProductIds) {
        $scope.products = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Product.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.products = result;
                $scope.productIds = "";
                if($scope.products) {
                    for(var i = 0; i < $scope.products.length; i++) {
                        $scope.productIds += (i == 0 ? "" : ",") + $scope.products[i].id;
                    }
                }
                console.log("productIds = " + $scope.productIds);
                ChannelPostsByProductIds.get({productIds: $scope.productIds}, function(result) {
                    console.log("result = " + result);
                    $scope.channelPosts = result;
                });
            });
        };

        $scope.getStatus = function(productId) {
            //console.log("productId = " + productId);
            if($scope.channelPosts) {
                var channelPost = $scope.channelPosts[productId];
                if(channelPost) {
                    //console.log("status = " + channelPost.status);
                    if(channelPost.status == 'Pending' || channelPost.status == 'Published' || channelPost.status == 'Error')
                        return channelPost.status;
                }
            }
            //console.log("status = none");
            return "none";
        };

        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Product.get({id: id}, function(result) {
                $scope.product = result;
                $('#deleteProductConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Product.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteProductConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.product = {productId: null, title: null, description: null, price: null, tax: null, currency: null, mainImage: null, mainImageAlt: null, id: null};
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
    });
