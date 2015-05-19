'use strict';

angular.module('mightymerceApp')
    .controller('OrderController', function ($scope, Order, User) {
        $scope.orders = [];
        $scope.users = User.query();
        $scope.loadAll = function() {
            Order.query(function(result) {
               $scope.orders = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Order.update($scope.order,
                function () {
                    $scope.loadAll();
                    $('#saveOrderModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Order.get({id: id}, function(result) {
                $scope.order = result;
                $('#saveOrderModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Order.get({id: id}, function(result) {
                $scope.order = result;
                $('#deleteOrderConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Order.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOrderConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.order = {test: null, test2: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
