'use strict';

angular.module('mightymerceApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('lineItem', {
                parent: 'entity',
                url: '/lineItem',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.lineItem.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lineItem/lineItems.html',
                        controller: 'LineItemController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lineItem');
                        return $translate.refresh();
                    }]
                }
            })
            .state('lineItemDetail', {
                parent: 'entity',
                url: '/lineItem/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.lineItem.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lineItem/lineItem-detail.html',
                        controller: 'LineItemDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lineItem');
                        return $translate.refresh();
                    }]
                }
            });
    });
