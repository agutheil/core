'use strict';

angular.module('mightymerceApp')
    .controller('SocialOrderController', function ($scope, SocialOrder, User) {
        $scope.socialOrders = [];
        $scope.users = User.query();
        $scope.loadAll = function() {
            SocialOrder.query(function(result) {
               $scope.socialOrders = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            SocialOrder.update($scope.socialOrder,
                function () {
                    $scope.loadAll();
                    $('#saveSocialOrderModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            SocialOrder.get({id: id}, function(result) {
                $scope.socialOrder = result;
                $('#saveSocialOrderModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            SocialOrder.get({id: id}, function(result) {
                $scope.socialOrder = result;
                $('#deleteSocialOrderConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SocialOrder.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSocialOrderConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.socialOrder = {test: null, test2: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
