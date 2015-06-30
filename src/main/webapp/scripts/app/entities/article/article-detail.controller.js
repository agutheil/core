'use strict';

angular.module('mightymerceApp')
    .controller('ArticleDetailController', function ($scope, $stateParams, Article, ChannelPost) {
        $scope.article = {};
        $scope.load = function (id) {
            Article.get({id: id}, function(result) {
              $scope.article = result;
            });
        };
        $scope.load($stateParams.id);
    });
