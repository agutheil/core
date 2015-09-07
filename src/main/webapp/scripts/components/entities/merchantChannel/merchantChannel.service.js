'use strict';

angular.module('coreApp')
    .factory('MerchantChannel', function ($resource, DateUtils) {
        return $resource('api/merchantChannels/:id', {}, {
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
