'use strict';

angular.module('coreApp')
    .controller('SocialOrderController', function ($scope, SocialOrder, ParseLinks) {
        $scope.socialOrders = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            SocialOrder.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.socialOrders = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            SocialOrder.get({id: id}, function(result) {
                $scope.socialOrder = result;
                $('#deleteSocialOrderConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SocialOrder.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSocialOrderConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.socialOrder = {transactionId: null, totalAmount: null, paymentStatus: null, deliveryStatus: null, orderStatus: null, id: null};
        };
    });
