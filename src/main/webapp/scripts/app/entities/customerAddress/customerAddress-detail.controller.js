'use strict';

angular.module('coreApp')
    .controller('CustomerAddressDetailController', function ($scope, $rootScope, $stateParams, entity, CustomerAddress, User) {
        $scope.customerAddress = entity;
        $scope.load = function (id) {
            CustomerAddress.get({id: id}, function(result) {
                $scope.customerAddress = result;
            });
        };
        $rootScope.$on('coreApp:customerAddressUpdate', function(event, result) {
            $scope.customerAddress = result;
        });
    });
