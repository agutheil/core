'use strict';

angular.module('coreApp').controller('MerchantChannelDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'MerchantChannel', 'User',
        function($scope, $stateParams, $modalInstance, entity, MerchantChannel, User) {

        $scope.merchantChannel = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            MerchantChannel.get({id : id}, function(result) {
                $scope.merchantChannel = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('coreApp:merchantChannelUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.merchantChannel.id != null) {
                MerchantChannel.update($scope.merchantChannel, onSaveFinished);
            } else {
                MerchantChannel.save($scope.merchantChannel, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
