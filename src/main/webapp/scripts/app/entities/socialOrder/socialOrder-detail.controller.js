'use strict';

angular.module('coreApp')
    .controller('SocialOrderDetailController', function ($scope, $rootScope, $stateParams, entity, SocialOrder, Article, Address, User) {
        $scope.socialOrder = entity;
        $scope.load = function (id) {
            SocialOrder.get({id: id}, function(result) {
                $scope.socialOrder = result;
            });
        };
        $rootScope.$on('coreApp:socialOrderUpdate', function(event, result) {
            $scope.socialOrder = result;
        });
    });
