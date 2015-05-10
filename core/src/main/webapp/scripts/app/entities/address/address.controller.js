'use strict';

angular.module('mightymerceApp')
    .controller('AddressController', function ($scope, Address, ParseLinks) {
        $scope.addresss = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Address.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.addresss = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Address.update($scope.address,
                function () {
                    $scope.loadAll();
                    $('#saveAddressModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Address.get({id: id}, function(result) {
                $scope.address = result;
                $('#saveAddressModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Address.get({id: id}, function(result) {
                $scope.address = result;
                $('#deleteAddressConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Address.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAddressConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.address = {street: null, no: null, zip: null, city: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
