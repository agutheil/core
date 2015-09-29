'use strict';

angular.module('coreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('deliveryOption', {
                parent: 'entity',
                url: '/deliveryOptions',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.deliveryOption.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/deliveryOption/deliveryOptions.html',
                        controller: 'DeliveryOptionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('deliveryOption');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('deliveryOption.detail', {
                parent: 'entity',
                url: '/deliveryOption/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.deliveryOption.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/deliveryOption/deliveryOption-detail.html',
                        controller: 'DeliveryOptionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('deliveryOption');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'DeliveryOption', function($stateParams, DeliveryOption) {
                        return DeliveryOption.get({id : $stateParams.id});
                    }]
                }
            })
            .state('deliveryOption.new', {
                parent: 'deliveryOption',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/deliveryOption/deliveryOption-dialog.html',
                        controller: 'DeliveryOptionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {title: null, within: null, country: null, taxrow: null, cost: null, currency: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('deliveryOption', null, { reload: true });
                    }, function() {
                        $state.go('deliveryOption');
                    })
                }]
            })
            .state('deliveryOption.edit', {
                parent: 'deliveryOption',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/deliveryOption/deliveryOption-dialog.html',
                        controller: 'DeliveryOptionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['DeliveryOption', function(DeliveryOption) {
                                return DeliveryOption.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('deliveryOption', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
