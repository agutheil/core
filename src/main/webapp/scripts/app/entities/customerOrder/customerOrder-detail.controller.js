'use strict';

angular.module('coreApp')
    .controller('CustomerOrderDetailController', function ($scope, $rootScope, $stateParams, entity, CustomerOrder, Product, Customer, CustomerAddress) {

        $scope.customerOrder = entity;

        $scope.loadData = function() {
            if($scope.customerOrder) {
                if($scope.customerOrder.productId) {
                    Product.get({id: $scope.customerOrder.productId}, function(product) {
                        $scope.product = product;
                    });
                    Customer.get({id: $scope.customerOrder.customerId}, function(customer) {
                        console.log("customer = " + JSON.stringify(customer));
                        if(customer) {
                            if(customer.billingAddressId) {
                                CustomerAddress.get({id: customer.billingAddressId}, function(customerAddress) {
                                    $scope.billingAddress = customerAddress;
                                });
                            }
                            if(customer.shippingAddressId) {
                                CustomerAddress.get({id: customer.shippingAddressId}, function(customerAddress) {
                                    $scope.shippingAddress = customerAddress;
                                });
                            }
                        }
                    });
                }
            }
        };

        $scope.load = function (id) {
            CustomerOrder.get({id: id}, function(result) {
                $scope.customerOrder = result;
                $scope.loadData();
            });
        };

        $scope.load($stateParams.id);

        $rootScope.$on('coreApp:customerOrderUpdate', function(event, result) {
            $scope.customerOrder = result;
            $scope.loadData();
        });

    });
