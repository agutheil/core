'use strict';

angular.module('mightymerceApp')
    .controller('SocialCartController', function ($scope, SocialCart, Customer, ParseLinks) {
        $scope.socialCarts = [];
        $scope.customers = Customer.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            SocialCart.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.socialCarts = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            SocialCart.update($scope.socialCart,
                function () {
                    $scope.loadAll();
                    $('#saveSocialCartModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            SocialCart.get({id: id}, function(result) {
                $scope.socialCart = result;
                $('#saveSocialCartModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            SocialCart.get({id: id}, function(result) {
                $scope.socialCart = result;
                $('#deleteSocialCartConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SocialCart.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSocialCartConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.socialCart = {id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
