'use strict';

angular.module('coreApp')
    .controller('CustomerOrderController', function ($scope, CustomerOrder, ParseLinks, CustomerMapByCustomerIds) {
        $scope.customerOrders = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            CustomerOrder.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.customerOrders = result;
                $scope.customerIds = "";
                if($scope.customerOrders) {
                    for(var i = 0; i < $scope.customerOrders.length; i++) {
                        $scope.customerIds += (i == 0 ? "" : ",") + $scope.customerOrders[i].customerId;
                    }
                }
                console.log("customerIds = " + $scope.customerIds);
                if($scope.customerIds) {
                    CustomerMapByCustomerIds.get({customerIds: $scope.customerIds}, function(result) {
                        console.log("result = " + result);
                        $scope.customerMapByCustomerIds = result;
                    });
                }
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.getCustomerName = function(customerId) {
            if($scope.customerMapByCustomerIds) {
                var customer = $scope.customerMapByCustomerIds[customerId];
                if(customer) {
                    return customer.lastName + ', ' + customer.firstName;
                }
            }
            return "";
        };

        $scope.delete = function (id) {
            CustomerOrder.get({id: id}, function(result) {
                $scope.customerOrder = result;
                $('#deleteCustomerOrderConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            CustomerOrder.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCustomerOrderConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.customerOrder = {placedOn: null, orderStatus: null, paymentStatus: null, totalAmount: null, tax: null, paypalAccountId: null, paypalTransactionId: null, currency: null, taxCurrency: null, id: null};
        };
    });
