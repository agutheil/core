'use strict';

angular.module('coreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('channelPost', {
                parent: 'entity',
                url: '/channelPosts',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.channelPost.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/channelPost/channelPosts.html',
                        controller: 'ChannelPostController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('channelPost');
                        $translatePartialLoader.addPart('publicationStatus');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('channelPost.detail', {
                parent: 'entity',
                url: '/channelPost/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.channelPost.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/channelPost/channelPost-detail.html',
                        controller: 'ChannelPostDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('channelPost');
                        $translatePartialLoader.addPart('publicationStatus');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ChannelPost', function($stateParams, ChannelPost) {
                        return ChannelPost.get({id : $stateParams.id});
                    }]
                }
            })
            .state('channelPost.new', {
                parent: 'channelPost',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/channelPost/channelPost-dialog.html',
                        controller: 'ChannelPostDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {status: null, publicationDate: null, externalPostKey: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('channelPost', null, { reload: true });
                    }, function() {
                        $state.go('channelPost');
                    })
                }]
            })
            .state('channelPost.edit', {
                parent: 'channelPost',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/channelPost/channelPost-dialog.html',
                        controller: 'ChannelPostDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ChannelPost', function(ChannelPost) {
                                return ChannelPost.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('channelPost', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
