'use strict';

angular.module('mightymerceApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('socialOrder', {
                parent: 'entity',
                url: '/socialOrder',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.socialOrder.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/socialOrder/socialOrders.html',
                        controller: 'SocialOrderController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('socialOrder');
                        return $translate.refresh();
                    }]
                }
            })
            .state('socialOrderDetail', {
                parent: 'entity',
                url: '/socialOrder/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.socialOrder.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/socialOrder/socialOrder-detail.html',
                        controller: 'SocialOrderDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('socialOrder');
                        return $translate.refresh();
                    }]
                }
            });
    });
