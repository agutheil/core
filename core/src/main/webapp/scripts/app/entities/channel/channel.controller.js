'use strict';

angular.module('mightymerceApp')
    .controller('ChannelController', function ($scope, Channel, ParseLinks) {
        $scope.channels = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Channel.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.channels = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Channel.update($scope.channel,
                function () {
                    $scope.loadAll();
                    $('#saveChannelModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Channel.get({id: id}, function(result) {
                $scope.channel = result;
                $('#saveChannelModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Channel.get({id: id}, function(result) {
                $scope.channel = result;
                $('#deleteChannelConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Channel.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteChannelConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.channel = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
