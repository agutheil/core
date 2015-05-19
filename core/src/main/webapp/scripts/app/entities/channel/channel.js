'use strict';

angular.module('mightymerceApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('channel', {
                parent: 'entity',
                url: '/channel',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.channel.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/channel/channels.html',
                        controller: 'ChannelController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('channel');
                        return $translate.refresh();
                    }]
                }
            })
            .state('channelDetail', {
                parent: 'entity',
                url: '/channel/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.channel.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/channel/channel-detail.html',
                        controller: 'ChannelDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('channel');
                        return $translate.refresh();
                    }]
                }
            });
    });
