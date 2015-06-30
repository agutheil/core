'use strict';

angular.module('mightymerceApp')
    .controller('LineItemDetailController', function ($scope, $stateParams, LineItem, Article, SocialCart) {
        $scope.lineItem = {};
        $scope.load = function (id) {
            LineItem.get({id: id}, function(result) {
              $scope.lineItem = result;
            });
        };
        $scope.load($stateParams.id);
    });
