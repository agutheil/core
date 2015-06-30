'use strict';

angular.module('mightymerceApp')
    .controller('SocialOrderDetailController', function ($scope, $stateParams, SocialOrder, User) {
        $scope.socialOrder = {};
        $scope.load = function (id) {
            SocialOrder.get({id: id}, function(result) {
              $scope.socialOrder = result;
            });
        };
        $scope.load($stateParams.id);
    });
