'use strict';

angular.module('coreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('merchantChannel', {
                parent: 'entity',
                url: '/merchantChannels',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.merchantChannel.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/merchantChannel/merchantChannels.html',
                        controller: 'MerchantChannelController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('merchantChannel');
                        $translatePartialLoader.addPart('channel');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('merchantChannel.detail', {
                parent: 'entity',
                url: '/merchantChannel/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.merchantChannel.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/merchantChannel/merchantChannel-detail.html',
                        controller: 'MerchantChannelDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('merchantChannel');
                        $translatePartialLoader.addPart('channel');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'MerchantChannel', function($stateParams, MerchantChannel) {
                        return MerchantChannel.get({id : $stateParams.id});
                    }]
                }
            })
            .state('merchantChannel.new', {
                parent: 'merchantChannel',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/merchantChannel/merchantChannel-dialog.html',
                        controller: 'MerchantChannelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {accessToken: null, channel: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('merchantChannel', null, { reload: true });
                    }, function() {
                        $state.go('merchantChannel');
                    })
                }]
            })
            .state('merchantChannel.edit', {
                parent: 'merchantChannel',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/merchantChannel/merchantChannel-dialog.html',
                        controller: 'MerchantChannelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MerchantChannel', function(MerchantChannel) {
                                return MerchantChannel.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('merchantChannel', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
