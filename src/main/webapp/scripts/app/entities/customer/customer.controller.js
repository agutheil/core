'use strict';

<<<<<<< HEAD
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
=======
angular.module('mightymerceApp')
    .controller('CustomerController', function ($scope, Customer, Address, ParseLinks) {
        $scope.customers = [];
        $scope.addresss = Address.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Customer.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.customers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Customer.update($scope.customer,
>>>>>>> customer und adresse
                function () {
                    $scope.loadAll();
                    $('#saveCustomerModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
<<<<<<< HEAD
            $scope.customer = Customer.get({id: id});
            $('#saveCustomerModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.customer = Customer.get({id: id});
            $('#deleteCustomerConfirmation').modal('show');
=======
            Customer.get({id: id}, function(result) {
                $scope.customer = result;
                $('#saveCustomerModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Customer.get({id: id}, function(result) {
                $scope.customer = result;
                $('#deleteCustomerConfirmation').modal('show');
            });
>>>>>>> customer und adresse
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
<<<<<<< HEAD
            $scope.customer = {firstName: null, lastName: null, street: null, streetNo: null, zip: null, city: null, country: null, eMail: null, id: null};
=======
            $scope.customer = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
>>>>>>> customer und adresse
        };
    });
