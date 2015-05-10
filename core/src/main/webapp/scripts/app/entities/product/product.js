'use strict';

angular.module('schubberApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('product', {
                parent: 'entity',
                url: '/product',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/product/products.html',
                        controller: 'ProductController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('product');
                        return $translate.refresh();
                    }]
                }
            })
            .state('productDetail', {
                parent: 'entity',
                url: '/product/:id',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/product/product-detail.html',
                        controller: 'ProductDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('product');
                        return $translate.refresh();
                    }]
                }
            });
    });
