'use strict';

angular.module('coreApp').controller('CustomerDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Customer', 'CustomerAddress', 'User',
        function($scope, $stateParams, $modalInstance, entity, Customer, CustomerAddress, User) {

        $scope.customer = entity;
        $scope.customeraddresss = CustomerAddress.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Customer.get({id : id}, function(result) {
                $scope.customer = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('coreApp:customerUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.customer.id != null) {
                Customer.update($scope.customer, onSaveFinished);
            } else {
                Customer.save($scope.customer, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
