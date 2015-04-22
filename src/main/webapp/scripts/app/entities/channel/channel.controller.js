'use strict';

<<<<<<< HEAD
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
=======
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
>>>>>>> neues domainmodell
                function () {
                    $scope.loadAll();
                    $('#saveChannelModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
<<<<<<< HEAD
            $scope.channel = Channel.get({id: id});
            $('#saveChannelModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.channel = Channel.get({id: id});
            $('#deleteChannelConfirmation').modal('show');
=======
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
>>>>>>> neues domainmodell
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
<<<<<<< HEAD
            $scope.channel = {type: null, name: null, token: null, id: null};
=======
            $scope.channel = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
>>>>>>> neues domainmodell
        };
    });
