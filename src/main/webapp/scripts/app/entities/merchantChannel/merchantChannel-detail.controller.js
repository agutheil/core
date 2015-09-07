'use strict';

angular.module('coreApp')
    .controller('MerchantChannelDetailController', function ($scope, $rootScope, $stateParams, entity, MerchantChannel, User) {
        $scope.merchantChannel = entity;
        $scope.load = function (id) {
            MerchantChannel.get({id: id}, function(result) {
                $scope.merchantChannel = result;
            });
        };
        $rootScope.$on('coreApp:merchantChannelUpdate', function(event, result) {
            $scope.merchantChannel = result;
        });
    });
