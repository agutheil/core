'use strict';

angular.module('coreApp')
    .factory('Customer', function ($resource, DateUtils) {
        return $resource('api/customers/:id', {}, {
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
    .factory('CustomerMapByCustomerIds', function ($resource) {
        return $resource('api/customerMapByCustomerIds', {}, {
            'get': { method:'GET', isArray:false }
        });
    });
