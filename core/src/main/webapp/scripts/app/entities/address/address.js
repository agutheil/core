'use strict';

angular.module('mightymerceApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('address', {
                parent: 'entity',
                url: '/address',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.address.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/address/addresss.html',
                        controller: 'AddressController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('address');
                        return $translate.refresh();
                    }]
                }
            })
            .state('addressDetail', {
                parent: 'entity',
                url: '/address/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'mightymerceApp.address.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/address/address-detail.html',
                        controller: 'AddressDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('address');
                        return $translate.refresh();
                    }]
                }
            });
    });
