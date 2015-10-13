'use strict';

angular.module('coreApp').controller('ChannelPostDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ChannelPost', 'User', 'MerchantChannel', 'Product',
        function($scope, $stateParams, $modalInstance, entity, ChannelPost, User, MerchantChannel, Product) {

        $scope.channelPost = entity;
        $scope.users = User.query();
        $scope.merchantchannels = MerchantChannel.query();
        $scope.products = Product.query();
        $scope.load = function(id) {
            ChannelPost.get({id : id}, function(result) {
                $scope.channelPost = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('coreApp:channelPostUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.channelPost.id != null) {
                ChannelPost.update($scope.channelPost, onSaveFinished);
            } else {
                ChannelPost.save($scope.channelPost, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
