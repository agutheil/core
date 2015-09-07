'use strict';

angular.module('coreApp')
    .controller('ArticleController', function ($scope, Article, ParseLinks) {
        $scope.articles = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Article.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.articles = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Article.get({id: id}, function(result) {
                $scope.article = result;
                $('#deleteArticleConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Article.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteArticleConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.article = {code: null, name: null, description: null, price: null, currency: null, id: null};
        };
    });
