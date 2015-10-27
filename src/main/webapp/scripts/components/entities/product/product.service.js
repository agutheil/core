'use strict';

angular.module('coreApp')
    .factory('Product', function ($resource) {
        return $resource('api/products/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });

angular.module('coreApp')
    .factory('ProductSold', function ($resource) {
        return $resource('api/productSold', {}, {
            'get': {
                method: 'GET',
                isArray: true
            }
        });
    });
