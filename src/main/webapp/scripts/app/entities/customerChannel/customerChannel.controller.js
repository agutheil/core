'use strict';

angular.module('mightymerceApp')
    .controller('CustomerChannelController', function ($scope, CustomerChannel, Customer, Channel, ParseLinks) {
        $scope.customerChannels = [];
        $scope.customers = Customer.query();
        $scope.channels = Channel.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            CustomerChannel.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.customerChannels = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            CustomerChannel.update($scope.customerChannel,
                function () {
                    $scope.loadAll();
                    $('#saveCustomerChannelModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            CustomerChannel.get({id: id}, function(result) {
                $scope.customerChannel = result;
                $('#saveCustomerChannelModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            CustomerChannel.get({id: id}, function(result) {
                $scope.customerChannel = result;
                $('#deleteCustomerChannelConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            CustomerChannel.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCustomerChannelConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.customerChannel = {key: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
