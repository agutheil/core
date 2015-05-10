'use strict';

angular.module('mightymerceApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('socialCart', {
                parent: 'entity',
                url: '/socialCart',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.socialCart.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/socialCart/socialCarts.html',
                        controller: 'SocialCartController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('socialCart');
                        return $translate.refresh();
                    }]
                }
            })
            .state('socialCartDetail', {
                parent: 'entity',
                url: '/socialCart/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.socialCart.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/socialCart/socialCart-detail.html',
                        controller: 'SocialCartDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('socialCart');
                        return $translate.refresh();
                    }]
                }
            });
    });
