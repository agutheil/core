'use strict';

angular.module('coreApp')
    .controller('MainController', function ($scope, Principal, TutorialStep, $location, CustomerOrderByOrderStatus, CustomerMapByCustomerIds, ProductSold, PerformanceStats) {

        $scope.tutorialSteps = TutorialStep.query();
        $scope.customerOrdersToBeFulfilledLimited = [];
        $scope.customerOrdersToBeFulfilledRemaining = [];
        $scope.customerOrdersToDisplay = [];

        $scope.productsSoldLimited = [];
        $scope.productsSoldRemaining = [];
        $scope.productsSoldToDisplay = [];

        $scope.loadPerformanceOption = 1;
        $scope.statCustomers = 0;
        $scope.statOrders = 0;
        $scope.statAverageCart = 0;
        $scope.statRevenue = 0;
        $scope.statSalesVolume = 0;
        $scope.statReturnVolume = 0;
        $scope.statStartDate = new Date();
        $scope.statEndDate = new Date();

        $scope.loadPerformanceStats = function(performanceOption) {
            $scope.loadPerformanceOption = performanceOption;
            if($scope.loadPerformanceOption == 4 && (!$scope.statStartDate || !$scope.statEndDate)) {
                return;
            }
            PerformanceStats.get({performanceStatOption: $scope.loadPerformanceOption, startDate: $scope.statStartDate, endDate: $scope.statEndDate}, function(result) {
                if(result) {
                    $scope.statCustomers = result.customerCount;
                    $scope.statOrders = result.orderCount;
                    $scope.statAverageCart = result.averageCart;
                    $scope.statRevenue = result.revenue;
                    $scope.statSalesVolume = result.salesVolume;
                    $scope.statReturnVolume = result.returnVolume;
                }
            });
        };

        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
            $scope.loadCustomerOrdersToBeFulfilled();
            $scope.loadProductsSold();
            $scope.loadPerformanceStats($scope.loadPerformanceOption);
/*
            if(Principal.isAuthenticated) {
                TutorialStep.get({user: $scope.account.id}, function(result) {
                    $scope.tutorialSteps = result;
                });
            }
*/
        });

        $scope.go = function(path) {
            $location.path(path);
        };

        $scope.showDashboard = function() {
            if($scope.tutorialSteps && $scope.tutorialSteps.length == 4) {
                for(var i=0; i<$scope.tutorialSteps.length; i++) {
                    if(!$scope.tutorialSteps[i].completed) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        };

        $scope.getId = function(productSold) {
            return productSold && productSold[0];
        };

        $scope.getTitle = function(productSold) {
            return productSold && productSold[1];
        };

        $scope.getTotalSale = function(productSold) {
            return productSold && productSold[2];
        };

        $scope.hasImage = function(productSold) {
            return productSold && productSold[3];
        };

        $scope.getImage = function(productSold) {
            return productSold && productSold[3];
        };

        $scope.loadProductsSold = function() {
            if($scope.isAuthenticated) {
                ProductSold.get({}, function(result) {
                    $scope.productsSoldLimited = result;
                    if($scope.productsSoldLimited.length > 3) {
                        $scope.productsSoldRemaining = $scope.productsSoldLimited.splice(3, $scope.productsSoldLimited.length);
                    }
                    $scope.productsSoldToDisplay = $scope.productsSoldLimited;
                });
            }
        };

        $scope.toggleShowAllProductsSold = function() {
            if($scope.productsSoldRemaining.length > 0) {
                if($scope.productsSoldToDisplay.length < ($scope.productsSoldLimited.length + $scope.productsSoldRemaining.length)) {
                    $scope.productsSoldToDisplay = $scope.productsSoldLimited.concat($scope.productsSoldRemaining);
                } else {
                    $scope.productsSoldToDisplay = $scope.productsSoldLimited;
                }
            }
        };

        $scope.displayShowAllProductsSold = function() {
            return $scope.productsSoldToDisplay.length < ($scope.productsSoldLimited.length + $scope.productsSoldRemaining.length);
        };

        $scope.displayShowLessProductsSold = function() {
            return !$scope.displayShowAllProductsSold();
        };

        $scope.loadCustomerOrdersToBeFulfilled = function() {
            if($scope.isAuthenticated) {
                CustomerOrderByOrderStatus.get({orderStatus: 'Fulfilled'}, function(result) {
                    $scope.customerOrdersToBeFulfilledLimited = result;
                    $scope.customerIds = "";
                    if($scope.customerOrdersToBeFulfilledLimited) {
                        for(var i = 0; i < $scope.customerOrdersToBeFulfilledLimited.length; i++) {
                            $scope.customerIds += (i == 0 ? "" : ",") + $scope.customerOrdersToBeFulfilledLimited[i].customerId;
                        }
                    }
                    if($scope.customerIds) {
                        CustomerMapByCustomerIds.get({customerIds: $scope.customerIds}, function(result) {
                            $scope.customerMapByCustomerIds = result;
                        });
                    }
                    if($scope.customerOrdersToBeFulfilledLimited.length > 3) {
                        $scope.customerOrdersToBeFulfilledRemaining = $scope.customerOrdersToBeFulfilledLimited.splice(3, $scope.customerOrdersToBeFulfilledLimited.length);
                    }
                    $scope.customerOrdersToDisplay = $scope.customerOrdersToBeFulfilledLimited;
                });
            }
        };

        $scope.toggleShowAll = function() {
            if($scope.customerOrdersToBeFulfilledRemaining.length > 0) {
                if($scope.customerOrdersToDisplay.length < ($scope.customerOrdersToBeFulfilledLimited.length + $scope.customerOrdersToBeFulfilledRemaining.length)) {
                    $scope.customerOrdersToDisplay = $scope.customerOrdersToBeFulfilledLimited.concat($scope.customerOrdersToBeFulfilledRemaining);
                } else {
                    $scope.customerOrdersToDisplay = $scope.customerOrdersToBeFulfilledLimited;
                }
            }
        };

        $scope.displayShowAll = function() {
            return $scope.customerOrdersToDisplay.length < ($scope.customerOrdersToBeFulfilledLimited.length + $scope.customerOrdersToBeFulfilledRemaining.length);
        };

        $scope.displayShowLess = function() {
            return !$scope.displayShowAll();
        };

        $scope.getCustomerName = function(customerId) {
            if($scope.customerMapByCustomerIds) {
                var customer = $scope.customerMapByCustomerIds[customerId];
                if(customer) {
                    return customer.lastName + ', ' + customer.firstName;
                }
            }
            return "";
        };


    });
