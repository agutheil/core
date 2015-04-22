'use strict';

<<<<<<< HEAD
angular.module('schubberApp')
    .controller('OrderController', function ($scope, Order) {
        $scope.orders = [];
=======
angular.module('mightymerceApp')
    .controller('OrderController', function ($scope, Order, User) {
        $scope.orders = [];
        $scope.users = User.query();
>>>>>>> neues domainmodell
        $scope.loadAll = function() {
            Order.query(function(result) {
               $scope.orders = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
<<<<<<< HEAD
            Order.save($scope.order,
=======
            Order.update($scope.order,
>>>>>>> neues domainmodell
                function () {
                    $scope.loadAll();
                    $('#saveOrderModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
<<<<<<< HEAD
            $scope.order = Order.get({id: id});
            $('#saveOrderModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.order = Order.get({id: id});
            $('#deleteOrderConfirmation').modal('show');
=======
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
>>>>>>> neues domainmodell
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
<<<<<<< HEAD
            $scope.order = {date: null, customer: null, paymentStatus: null, orderStatus: null, id: null};
=======
            $scope.order = {test: null, test2: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
>>>>>>> neues domainmodell
        };
    });
