'use strict';

angular.module('mightymerceApp')
    .controller('LineItemController', function ($scope, LineItem, Article, SocialCart, ParseLinks) {
        $scope.lineItems = [];
        $scope.articles = Article.query();
        $scope.socialcarts = SocialCart.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            LineItem.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.lineItems = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            LineItem.update($scope.lineItem,
                function () {
                    $scope.loadAll();
                    $('#saveLineItemModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            LineItem.get({id: id}, function(result) {
                $scope.lineItem = result;
                $('#saveLineItemModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            LineItem.get({id: id}, function(result) {
                $scope.lineItem = result;
                $('#deleteLineItemConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            LineItem.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteLineItemConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.lineItem = {amount: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
