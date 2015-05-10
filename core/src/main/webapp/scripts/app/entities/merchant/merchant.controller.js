'use strict';

angular.module('mightymerceApp')
    .controller('MerchantController', function ($scope, Merchant, Address, ParseLinks) {
        $scope.merchants = [];
        $scope.addresss = Address.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Merchant.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.merchants = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Merchant.update($scope.merchant,
                function () {
                    $scope.loadAll();
                    $('#saveMerchantModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Merchant.get({id: id}, function(result) {
                $scope.merchant = result;
                $('#saveMerchantModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Merchant.get({id: id}, function(result) {
                $scope.merchant = result;
                $('#deleteMerchantConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Merchant.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMerchantConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.merchant = {name: null, prename: null, firma: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
