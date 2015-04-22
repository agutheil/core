'use strict';

<<<<<<< HEAD
angular.module('schubberApp')
    .controller('OrderDetailController', function ($scope, $stateParams, Order) {
=======
angular.module('mightymerceApp')
    .controller('OrderDetailController', function ($scope, $stateParams, Order, User) {
>>>>>>> neues domainmodell
        $scope.order = {};
        $scope.load = function (id) {
            Order.get({id: id}, function(result) {
              $scope.order = result;
            });
        };
        $scope.load($stateParams.id);
    });
