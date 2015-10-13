'use strict';

angular.module('coreApp')
    .controller('ProductDetailController', function ($scope, $rootScope, $stateParams, entity, Product, User, DeliveryOption) {
        $scope.product = entity;
        $scope.load = function (id) {
            Product.get({id: id}, function(result) {
                $scope.product = result;
            });
        };
        $rootScope.$on('coreApp:productUpdate', function(event, result) {
            $scope.product = result;
        });
    });
