'use strict';

angular.module('coreApp')
    .controller('MerchantChannelController', function ($scope, MerchantChannel, ParseLinks) {
        $scope.merchantChannels = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            MerchantChannel.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.merchantChannels = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            MerchantChannel.get({id: id}, function(result) {
                $scope.merchantChannel = result;
                $('#deleteMerchantChannelConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            MerchantChannel.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMerchantChannelConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.merchantChannel = {accessToken: null, channel: null, id: null};
        };
    });
