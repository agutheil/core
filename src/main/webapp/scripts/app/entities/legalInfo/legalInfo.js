'use strict';

angular.module('coreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('legalInfo', {
                parent: 'entity',
                url: '/legalInfos',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.legalInfo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/legalInfo/legalInfos.html',
                        controller: 'LegalInfoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('legalInfo');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('legalInfo.detail', {
                parent: 'entity',
                url: '/legalInfo/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.legalInfo.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/legalInfo/legalInfo-detail.html',
                        controller: 'LegalInfoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('legalInfo');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'LegalInfo', function($stateParams, LegalInfo) {
                        return LegalInfo.get({id : $stateParams.id});
                    }]
                }
            })
            .state('legalInfo.new', {
                parent: 'legalInfo',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/legalInfo/legalInfo-dialog.html',
                        controller: 'LegalInfoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {title: null, purpose: null, pageTitle: null, pageText: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('legalInfo', null, { reload: true });
                    }, function() {
                        $state.go('legalInfo');
                    })
                }]
            })
            .state('legalInfo.edit', {
                parent: 'legalInfo',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/legalInfo/legalInfo-dialog.html',
                        controller: 'LegalInfoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['LegalInfo', function(LegalInfo) {
                                return LegalInfo.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('legalInfo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
