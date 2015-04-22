'use strict';

<<<<<<< HEAD
angular.module('schubberApp')
=======
angular.module('mightymerceApp')
>>>>>>> neues domainmodell
    .config(function ($stateProvider) {
        $stateProvider
            .state('channel', {
                parent: 'entity',
                url: '/channel',
                data: {
<<<<<<< HEAD
                    roles: ['ROLE_USER']
=======
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.channel.home.title'
>>>>>>> neues domainmodell
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
<<<<<<< HEAD
                    roles: ['ROLE_USER']
=======
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.channel.detail.title'
>>>>>>> neues domainmodell
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
