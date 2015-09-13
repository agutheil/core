'use strict';

angular.module('coreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('socialOrder', {
                parent: 'entity',
                url: '/socialOrders',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.socialOrder.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/socialOrder/socialOrders.html',
                        controller: 'SocialOrderController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('socialOrder');
                        $translatePartialLoader.addPart('deliveryStatus');
                        $translatePartialLoader.addPart('orderStatus');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('socialOrder.detail', {
                parent: 'entity',
                url: '/socialOrder/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.socialOrder.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/socialOrder/socialOrder-detail.html',
                        controller: 'SocialOrderDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('socialOrder');
                        $translatePartialLoader.addPart('deliveryStatus');
                        $translatePartialLoader.addPart('orderStatus');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SocialOrder', function($stateParams, SocialOrder) {
                        return SocialOrder.get({id : $stateParams.id});
                    }]
                }
            })
            .state('socialOrder.new', {
                parent: 'socialOrder',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/socialOrder/socialOrder-dialog.html',
                        controller: 'SocialOrderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {transactionId: null, totalAmount: null, paymentStatus: null, deliveryStatus: null, orderStatus: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('socialOrder', null, { reload: true });
                    }, function() {
                        $state.go('socialOrder');
                    })
                }]
            })
            .state('socialOrder.edit', {
                parent: 'socialOrder',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/socialOrder/socialOrder-dialog.html',
                        controller: 'SocialOrderDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SocialOrder', function(SocialOrder) {
                                return SocialOrder.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('socialOrder', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
