'use strict';

angular.module('coreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('customerOrder', {
                parent: 'entity',
                url: '/customerOrders',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.customerOrder.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/customerOrder/customerOrders.html',
                        controller: 'CustomerOrderController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('customerOrder');
                        $translatePartialLoader.addPart('orderStatus');
                        $translatePartialLoader.addPart('paymentStatus');
                        $translatePartialLoader.addPart('tax');
                        $translatePartialLoader.addPart('currency');
                        $translatePartialLoader.addPart('currency');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('customerOrder.detail', {
                parent: 'entity',
                url: '/customerOrder/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.customerOrder.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/customerOrder/customerOrder-detail.html',
                        controller: 'CustomerOrderDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('customerOrder');
                        $translatePartialLoader.addPart('orderStatus');
                        $translatePartialLoader.addPart('paymentStatus');
                        $translatePartialLoader.addPart('tax');
                        $translatePartialLoader.addPart('currency');
                        $translatePartialLoader.addPart('currency');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'CustomerOrder', function($stateParams, CustomerOrder) {
                        return CustomerOrder.get({id : $stateParams.id});
                    }]
                }
            })
            .state('customerOrder.new', {
                parent: 'customerOrder',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/customerOrder/customerOrder-dialog.html',
                        controller: 'CustomerOrderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {placedOn: null, orderStatus: null, paymentStatus: null, totalAmount: null, tax: null, paypalAccountId: null, paypalTransactionId: null, currency: null, taxCurrency: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('customerOrder', null, { reload: true });
                    }, function() {
                        $state.go('customerOrder');
                    })
                }]
            })
            .state('customerOrder.edit', {
                parent: 'customerOrder',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/customerOrder/customerOrder-dialog.html',
                        controller: 'CustomerOrderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CustomerOrder', function(CustomerOrder) {
                                return CustomerOrder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('customerOrder', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('customerOrder.fulfill', {
                parent: 'customerOrder',
                url: '/{id}/fulfill',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/customerOrder/customerOrder-fulfill.html',
                        controller: 'CustomerOrderFulfillController',
                        size: 'md',
                        resolve: {
                            entity: ['CustomerOrder', function(CustomerOrder) {
                                return CustomerOrder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                            $state.go('customerOrder', null, { reload: true });
                        }, function() {
                            $state.go('^', {});
                        })
                }]
            })
            .state('customerOrder.fulfillDetail', {
                parent: 'customerOrder.detail',
                url: '/{id}/fulfill',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/customerOrder/customerOrder-fulfill.html',
                        controller: 'CustomerOrderFulfillController',
                        size: 'md',
                        resolve: {
                            entity: ['CustomerOrder', function(CustomerOrder) {
                                return CustomerOrder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                            $state.go('customerOrder', null, { reload: true });
                        }, function() {
                            $state.go('^', {});
                        })
                }]
            })
            .state('customerOrder.return', {
                parent: 'customerOrder',
                url: '/{id}/return',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/customerOrder/customerOrder-return.html',
                        controller: 'CustomerOrderReturnController',
                        size: 'md',
                        resolve: {
                            entity: ['CustomerOrder', function(CustomerOrder) {
                                return CustomerOrder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                            $state.go('customerOrder', null, { reload: true });
                        }, function() {
                            $state.go('^', {});
                        })
                }]
            })
            .state('customerOrder.returnDetail', {
                parent: 'customerOrder.detail',
                url: '/{id}/return',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/customerOrder/customerOrder-return.html',
                        controller: 'CustomerOrderReturnController',
                        size: 'md',
                        resolve: {
                            entity: ['CustomerOrder', function(CustomerOrder) {
                                return CustomerOrder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                            $state.go('customerOrder', null, { reload: true });
                        }, function() {
                            $state.go('^', {});
                        })
                }]
            })
            .state('customerOrder.cancel', {
                parent: 'customerOrder.detail',
                url: '/{id}/cancel',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/customerOrder/customerOrder-cancel.html',
                        controller: 'CustomerOrderCancelController',
                        size: 'md',
                        resolve: {
                            entity: ['CustomerOrder', function(CustomerOrder) {
                                return CustomerOrder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                            //$state.go('customerOrder', null, { reload: true });
                            $state.go('customerOrder', null, { reload: true });
                        }, function() {
                            $state.go('^', {});
                            //$state.go('customerOrder.detail', {id : $stateParams.id}, { reload: true });
                        })
                }]
            })
        ;
    });
