'use strict';

angular.module('schubberApp')
    .controller('OrderDetailController', function ($scope, $stateParams, Order) {
        $scope.order = {};
        $scope.load = function (id) {
            Order.get({id: id}, function(result) {
              $scope.order = result;
            });
        };
        $scope.load($stateParams.id);
    });
