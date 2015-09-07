'use strict';

angular.module('coreApp')
    .controller('ChannelPostController', function ($scope, ChannelPost, ParseLinks) {
        $scope.channelPosts = [];
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

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.channelPost = {status: null, publicationDate: null, externalPostKey: null, id: null};
        };
    });
