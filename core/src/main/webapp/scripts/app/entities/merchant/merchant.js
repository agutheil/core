'use strict';

angular.module('mightymerceApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('merchant', {
                parent: 'entity',
                url: '/merchant',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.merchant.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/merchant/merchants.html',
                        controller: 'MerchantController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('merchant');
                        return $translate.refresh();
                    }]
                }
            })
            .state('merchantDetail', {
                parent: 'entity',
                url: '/merchant/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.merchant.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/merchant/merchant-detail.html',
                        controller: 'MerchantDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('merchant');
                        return $translate.refresh();
                    }]
                }
            });
    });
