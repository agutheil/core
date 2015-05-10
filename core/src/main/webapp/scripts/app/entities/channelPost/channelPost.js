'use strict';

angular.module('mightymerceApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('channelPost', {
                parent: 'entity',
                url: '/channelPost',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.channelPost.home.title'
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
                        return $translate.refresh();
                    }]
                }
            })
            .state('channelPostDetail', {
                parent: 'entity',
                url: '/channelPost/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.channelPost.detail.title'
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
                        return $translate.refresh();
                    }]
                }
            });
    });
