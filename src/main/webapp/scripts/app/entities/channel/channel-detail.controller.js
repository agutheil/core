'use strict';

angular.module('schubberApp')
    .controller('ChannelDetailController', function ($scope, $stateParams, Channel) {
        $scope.channel = {};
        $scope.load = function (id) {
            Channel.get({id: id}, function(result) {
              $scope.channel = result;
            });
        };
        $scope.load($stateParams.id);
    });
