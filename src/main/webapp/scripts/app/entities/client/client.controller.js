'use strict';

angular.module('mightymerceApp')
    .controller('ClientController', function ($scope, Client, Order) {
        $scope.clients = [];
        $scope.orders = Order.query();
        $scope.loadAll = function() {
            Client.query(function(result) {
               $scope.clients = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Client.update($scope.client,
                function () {
                    $scope.loadAll();
                    $('#saveClientModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Client.get({id: id}, function(result) {
                $scope.client = result;
                $('#saveClientModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Client.get({id: id}, function(result) {
                $scope.client = result;
                $('#deleteClientConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Client.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteClientConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.client = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
