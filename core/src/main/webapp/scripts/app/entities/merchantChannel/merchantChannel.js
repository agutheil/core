'use strict';

angular.module('mightymerceApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('merchantChannel', {
                parent: 'entity',
                url: '/merchantChannel',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.merchantChannel.home.title'
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
                        return $translate.refresh();
                    }]
                }
            })
            .state('merchantChannelDetail', {
                parent: 'entity',
                url: '/merchantChannel/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.merchantChannel.detail.title'
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
                        return $translate.refresh();
                    }]
                }
            });
    });
