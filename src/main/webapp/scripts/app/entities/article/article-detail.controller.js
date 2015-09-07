'use strict';

angular.module('coreApp')
    .controller('ArticleDetailController', function ($scope, $rootScope, $stateParams, entity, Article, User) {
        $scope.article = entity;
        $scope.load = function (id) {
            Article.get({id: id}, function(result) {
                $scope.article = result;
            });
        };
        $rootScope.$on('coreApp:articleUpdate', function(event, result) {
            $scope.article = result;
        });
    });
