'use strict';

angular.module('coreApp')
    .controller('DeliveryOptionDetailController', function ($scope, $rootScope, $stateParams, entity, DeliveryOption, User) {
        $scope.deliveryOption = entity;
        $scope.load = function (id) {
            DeliveryOption.get({id: id}, function(result) {
                $scope.deliveryOption = result;
            });
        };
        $rootScope.$on('coreApp:deliveryOptionUpdate', function(event, result) {
            $scope.deliveryOption = result;
        });
    });
