'use strict';

angular.module('coreApp')
    .controller('AddressDetailController', function ($scope, $rootScope, $stateParams, entity, Address, User) {
        $scope.address = entity;
        $scope.load = function (id) {
            Address.get({id: id}, function(result) {
                $scope.address = result;
            });
        };
        $rootScope.$on('coreApp:addressUpdate', function(event, result) {
            $scope.address = result;
        });
    });
