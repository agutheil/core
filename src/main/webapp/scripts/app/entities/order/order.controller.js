'use strict';

angular.module('schubberApp')
    .controller('OrderController', function ($scope, Order) {
        $scope.orders = [];
        $scope.loadAll = function() {
            Order.query(function(result) {
               $scope.orders = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Order.save($scope.order,
                function () {
                    $scope.loadAll();
                    $('#saveOrderModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.order = Order.get({id: id});
            $('#saveOrderModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.order = Order.get({id: id});
            $('#deleteOrderConfirmation').modal('show');
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
            $scope.order = {date: null, customer: null, paymentStatus: null, orderStatus: null, id: null};
        };
    });
