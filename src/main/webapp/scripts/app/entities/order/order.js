'use strict';

<<<<<<< HEAD
angular.module('schubberApp')
=======
angular.module('mightymerceApp')
>>>>>>> neues domainmodell
    .config(function ($stateProvider) {
        $stateProvider
            .state('order', {
                parent: 'entity',
                url: '/order',
                data: {
<<<<<<< HEAD
                    roles: ['ROLE_USER']
=======
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.order.home.title'
>>>>>>> neues domainmodell
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/order/orders.html',
                        controller: 'OrderController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('order');
                        return $translate.refresh();
                    }]
                }
            })
            .state('orderDetail', {
                parent: 'entity',
                url: '/order/:id',
                data: {
<<<<<<< HEAD
                    roles: ['ROLE_USER']
=======
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.order.detail.title'
>>>>>>> neues domainmodell
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/order/order-detail.html',
                        controller: 'OrderDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('order');
                        return $translate.refresh();
                    }]
                }
            });
    });
