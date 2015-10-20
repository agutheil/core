'use strict';

angular.module('coreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('customerAddress', {
                parent: 'entity',
                url: '/customerAddresss',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.customerAddress.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/customerAddress/customerAddresss.html',
                        controller: 'CustomerAddressController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('customerAddress');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('customerAddress.detail', {
                parent: 'entity',
                url: '/customerAddress/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.customerAddress.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/customerAddress/customerAddress-detail.html',
                        controller: 'CustomerAddressDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('customerAddress');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'CustomerAddress', function($stateParams, CustomerAddress) {
                        return CustomerAddress.get({id : $stateParams.id});
                    }]
                }
            })
            .state('customerAddress.new', {
                parent: 'customerAddress',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/customerAddress/customerAddress-dialog.html',
                        controller: 'CustomerAddressDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {addressTo: null, streetAddress: null, zip: null, city: null, state: null, country: null, status: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('customerAddress', null, { reload: true });
                    }, function() {
                        $state.go('customerAddress');
                    })
                }]
            })
            .state('customerAddress.edit', {
                parent: 'customerAddress',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/customerAddress/customerAddress-dialog.html',
                        controller: 'CustomerAddressDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CustomerAddress', function(CustomerAddress) {
                                return CustomerAddress.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('customerAddress', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
