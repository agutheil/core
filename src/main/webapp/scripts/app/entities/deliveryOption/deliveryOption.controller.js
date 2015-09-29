'use strict';

angular.module('coreApp')
    .controller('DeliveryOptionController', function ($scope, DeliveryOption) {
        $scope.deliveryOptions = [];
        $scope.loadAll = function() {
            DeliveryOption.query(function(result) {
               $scope.deliveryOptions = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            DeliveryOption.get({id: id}, function(result) {
                $scope.deliveryOption = result;
                $('#deleteDeliveryOptionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            DeliveryOption.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteDeliveryOptionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.deliveryOption = {title: null, within: null, country: null, taxrow: null, cost: null, currency: null, id: null};
        };
    });
