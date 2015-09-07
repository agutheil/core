'use strict';

angular.module('coreApp')
    .controller('CustomerDetailController', function ($scope, $rootScope, $stateParams, entity, Customer, User) {
        $scope.customer = entity;
        $scope.load = function (id) {
            Customer.get({id: id}, function(result) {
                $scope.customer = result;
            });
        };
        $rootScope.$on('coreApp:customerUpdate', function(event, result) {
            $scope.customer = result;
        });
    });
