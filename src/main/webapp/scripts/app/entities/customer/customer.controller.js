'use strict';

angular.module('schubberApp')
    .controller('CustomerController', function ($scope, Customer) {
        $scope.customers = [];
        $scope.loadAll = function() {
            Customer.query(function(result) {
               $scope.customers = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Customer.save($scope.customer,
                function () {
                    $scope.loadAll();
                    $('#saveCustomerModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.customer = Customer.get({id: id});
            $('#saveCustomerModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.customer = Customer.get({id: id});
            $('#deleteCustomerConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            Customer.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCustomerConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.customer = {firstName: null, lastName: null, street: null, streetNo: null, zip: null, city: null, country: null, eMail: null, id: null};
        };
    });
