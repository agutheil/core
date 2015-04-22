'use strict';

angular.module('mightymerceApp')
    .controller('CustomerDetailController', function ($scope, $stateParams, Customer, Address) {
        $scope.customer = {};
        $scope.load = function (id) {
            Customer.get({id: id}, function(result) {
              $scope.customer = result;
            });
        };
        $scope.load($stateParams.id);
    });
