'use strict';

angular.module('coreApp')
    .controller('ChannelPostDetailController', function ($scope, $rootScope, $stateParams, entity, ChannelPost, User, MerchantChannel, Product) {
        $scope.channelPost = entity;
        $scope.load = function (id) {
            ChannelPost.get({id: id}, function(result) {
                $scope.channelPost = result;
            });
        };
        $rootScope.$on('coreApp:channelPostUpdate', function(event, result) {
            $scope.channelPost = result;
        });
    });
