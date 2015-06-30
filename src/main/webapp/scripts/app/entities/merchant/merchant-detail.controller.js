'use strict';

angular.module('mightymerceApp')
    .controller('MerchantDetailController', function ($scope, $stateParams, Merchant, Address) {
        $scope.merchant = {};
        $scope.load = function (id) {
            Merchant.get({id: id}, function(result) {
              $scope.merchant = result;
            });
        };
        $scope.load($stateParams.id);
    });
