'use strict';

angular.module('coreApp').controller('CustomerAddressDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'CustomerAddress', 'User',
        function($scope, $stateParams, $modalInstance, entity, CustomerAddress, User) {

        $scope.customerAddress = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            CustomerAddress.get({id : id}, function(result) {
                $scope.customerAddress = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('coreApp:customerAddressUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.customerAddress.id != null) {
                CustomerAddress.update($scope.customerAddress, onSaveFinished);
            } else {
                CustomerAddress.save($scope.customerAddress, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
