'use strict';

angular.module('coreApp')
    .factory('DeliveryOption', function ($resource, DateUtils) {
        return $resource('api/deliveryOptions/:id', {}, {
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
