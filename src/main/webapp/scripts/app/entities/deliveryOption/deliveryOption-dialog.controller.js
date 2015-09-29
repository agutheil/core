'use strict';

angular.module('coreApp').controller('DeliveryOptionDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'DeliveryOption', 'User',
        function($scope, $stateParams, $modalInstance, entity, DeliveryOption, User) {

        $scope.deliveryOption = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            DeliveryOption.get({id : id}, function(result) {
                $scope.deliveryOption = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('coreApp:deliveryOptionUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.deliveryOption.id != null) {
                DeliveryOption.update($scope.deliveryOption, onSaveFinished);
            } else {
                DeliveryOption.save($scope.deliveryOption, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
