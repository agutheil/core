'use strict';

angular.module('coreApp').controller('CustomerOrderFulfillController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'CustomerOrder',
        function($scope, $stateParams, $modalInstance, entity, CustomerOrder) {

            $scope.customerOrder = entity;

            $scope.load = function(id) {
                CustomerOrder.get({id : id}, function(result) {
                    $scope.customerOrder = result;
                });
            };
            $scope.load($stateParams.id);

            var onSaveSuccess = function (result) {
                $scope.$emit('coreApp:customerOrderUpdate', result);
                $modalInstance.close(result);
            };

            var onSaveError = function (result) {
                $scope.load($stateParams.id);
            };

            $scope.save = function () {
                $scope.customerOrder.orderStatus = 'Fulfilled';
                CustomerOrder.update($scope.customerOrder, onSaveSuccess, onSaveError);
            };

            $scope.clear = function() {
                $modalInstance.dismiss('cancel');
            };

        }]);
