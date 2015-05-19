'use strict';

angular.module('mightymerceApp')
    .controller('MerchantChannelDetailController', function ($scope, $stateParams, MerchantChannel, Merchant, Channel) {
        $scope.merchantChannel = {};
        $scope.load = function (id) {
            MerchantChannel.get({id: id}, function(result) {
              $scope.merchantChannel = result;
            });
        };
        $scope.load($stateParams.id);
    });
