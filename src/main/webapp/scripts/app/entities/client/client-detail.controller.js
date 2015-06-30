'use strict';

angular.module('mightymerceApp')
    .controller('ClientDetailController', function ($scope, $stateParams, Client, Order) {
        $scope.client = {};
        $scope.load = function (id) {
            Client.get({id: id}, function(result) {
              $scope.client = result;
            });
        };
        $scope.load($stateParams.id);
    });
