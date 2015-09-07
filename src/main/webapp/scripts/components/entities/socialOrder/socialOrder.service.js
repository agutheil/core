'use strict';

angular.module('coreApp')
    .factory('SocialOrder', function ($resource, DateUtils) {
        return $resource('api/socialOrders/:id', {}, {
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
