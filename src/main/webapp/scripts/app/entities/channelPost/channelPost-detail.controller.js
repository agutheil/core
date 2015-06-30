'use strict';

angular.module('mightymerceApp')
    .controller('ChannelPostDetailController', function ($scope, $stateParams, ChannelPost, Article, MerchantChannel) {
        $scope.channelPost = {};
        $scope.load = function (id) {
            ChannelPost.get({id: id}, function(result) {
              $scope.channelPost = result;
            });
        };
        $scope.load($stateParams.id);
    });
