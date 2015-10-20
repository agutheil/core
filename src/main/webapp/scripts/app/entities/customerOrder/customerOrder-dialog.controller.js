'use strict';

angular.module('coreApp').controller('CustomerOrderDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'CustomerOrder', 'Product', 'Customer', 'User', 'Lookup',
        function($scope, $stateParams, $modalInstance, entity, CustomerOrder, Product, Customer, User, Lookup) {

        $scope.customerOrder = entity;
        $scope.products = Product.query();
        $scope.customers = Customer.query();
        $scope.users = User.query();
        $scope.taxes = Lookup.get({lookupName : 'tax'});
        $scope.currencies = Lookup.get({lookupName : 'currency'});
        $scope.load = function(id) {
            CustomerOrder.get({id : id}, function(result) {
                $scope.customerOrder = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('coreApp:customerOrderUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.customerOrder.id != null) {
                CustomerOrder.update($scope.customerOrder, onSaveFinished);
            } else {
                CustomerOrder.save($scope.customerOrder, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
