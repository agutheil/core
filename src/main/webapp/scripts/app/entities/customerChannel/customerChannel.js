'use strict';

angular.module('mightymerceApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('customerChannel', {
                parent: 'entity',
                url: '/customerChannel',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.customerChannel.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/customerChannel/customerChannels.html',
                        controller: 'CustomerChannelController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('customerChannel');
                        return $translate.refresh();
                    }]
                }
            })
            .state('customerChannelDetail', {
                parent: 'entity',
                url: '/customerChannel/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.customerChannel.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/customerChannel/customerChannel-detail.html',
                        controller: 'CustomerChannelDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('customerChannel');
                        return $translate.refresh();
                    }]
                }
            });
    });
