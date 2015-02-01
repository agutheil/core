'use strict';

angular.module('schubberApp')
    .controller('ChannelController', function ($scope, Channel) {
        $scope.channels = [];
        $scope.loadAll = function() {
            Channel.query(function(result) {
               $scope.channels = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Channel.save($scope.channel,
                function () {
                    $scope.loadAll();
                    $('#saveChannelModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.channel = Channel.get({id: id});
            $('#saveChannelModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.channel = Channel.get({id: id});
            $('#deleteChannelConfirmation').modal('show');
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
            $scope.channel = {type: null, name: null, token: null, id: null};
        };
    });
