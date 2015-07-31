'use strict';

angular.module('mightymerceApp')
    .controller('MerchantChannelDetailController', function ($scope, $stateParams, MerchantChannel, Merchant, Channel, Facebook) {
        $scope.merchantChannel = {};
        $scope.load = function (id) {
            MerchantChannel.get({id: id}, function(result) {
              $scope.merchantChannel = result;
            });
        };
        $scope.getMyLastName = function() {
            Facebook.getMyLastName()
                .then(function(response) {
                    $scope.last_name = response.last_name;
                    console.log($scope.last_name)
            });
        };
        $scope.load($stateParams.id);
        $scope.getMyLastName();
    });
