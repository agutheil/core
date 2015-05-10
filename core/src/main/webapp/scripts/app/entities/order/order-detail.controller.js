'use strict';

angular.module('mightymerceApp')
    .controller('OrderDetailController', function ($scope, $stateParams, Order, User) {
        $scope.order = {};
        $scope.load = function (id) {
            Order.get({id: id}, function(result) {
              $scope.order = result;
            });
        };
        $scope.load($stateParams.id);
    });
