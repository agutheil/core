'use strict';

angular.module('coreApp')
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

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.address = {addressee: null, detailedDescription: null, streetname: null, housenumber: null, postalCode: null, town: null, country: null, id: null};
        };
    });
