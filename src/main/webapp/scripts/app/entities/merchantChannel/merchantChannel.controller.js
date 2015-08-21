'use strict';

angular.module('mightymerceApp')
    .controller('MerchantChannelController', function ($scope, MerchantChannel, Merchant, Channel, ParseLinks, Facebook) {
        $scope.merchantChannels = [];
        $scope.merchants = Merchant.query();
        $scope.channels = Channel.query();
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

        $scope.create = function () {
            MerchantChannel.update($scope.merchantChannel,
                function () {
                    $scope.loadAll();
                    $('#saveMerchantChannelModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            MerchantChannel.get({id: id}, function(result) {
                $scope.merchantChannel = result;
                $('#saveMerchantChannelModal').modal('show');
            });
        };

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

        $scope.clear = function () {
            $scope.merchantChannel = {accessToken: null, name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };

        $scope.$watch(function () { return Facebook.facebookToken; },
            function (value) {
                console.log("In $watch - facebookToken:" + value);
                $scope.merchantChannel.accessToken = value;
            }
        );
    });
