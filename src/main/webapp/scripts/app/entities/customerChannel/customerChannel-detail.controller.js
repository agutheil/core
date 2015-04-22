'use strict';

angular.module('mightymerceApp')
    .controller('CustomerChannelDetailController', function ($scope, $stateParams, CustomerChannel, Customer, Channel) {
        $scope.customerChannel = {};
        $scope.load = function (id) {
            CustomerChannel.get({id: id}, function(result) {
              $scope.customerChannel = result;
            });
        };
        $scope.load($stateParams.id);
    });
