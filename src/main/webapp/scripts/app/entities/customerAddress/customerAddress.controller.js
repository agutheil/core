'use strict';

angular.module('coreApp')
    .controller('CustomerAddressController', function ($scope, CustomerAddress, ParseLinks) {
        $scope.customerAddresss = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            CustomerAddress.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.customerAddresss = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            CustomerAddress.get({id: id}, function(result) {
                $scope.customerAddress = result;
                $('#deleteCustomerAddressConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            CustomerAddress.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCustomerAddressConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.customerAddress = {addressTo: null, streetAddress: null, zip: null, city: null, state: null, country: null, status: null, id: null};
        };
    });
