'use strict';

angular.module('mightymerceApp')
    .controller('ChannelPostController', function ($scope, ChannelPost, Article, CustomerChannel, ParseLinks) {
        $scope.channelPosts = [];
        $scope.articles = Article.query();
        $scope.customerchannels = CustomerChannel.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            ChannelPost.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.channelPosts = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            ChannelPost.update($scope.channelPost,
                function () {
                    $scope.loadAll();
                    $('#saveChannelPostModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            ChannelPost.get({id: id}, function(result) {
                $scope.channelPost = result;
                $('#saveChannelPostModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            ChannelPost.get({id: id}, function(result) {
                $scope.channelPost = result;
                $('#deleteChannelPostConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ChannelPost.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteChannelPostConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.channelPost = {status: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
