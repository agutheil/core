'use strict';

angular.module('mightymerceApp')
    .controller('SocialCartDetailController', function ($scope, $stateParams, SocialCart, Customer) {
        $scope.socialCart = {};
        $scope.load = function (id) {
            SocialCart.get({id: id}, function(result) {
              $scope.socialCart = result;
            });
        };
        $scope.load($stateParams.id);
    });
