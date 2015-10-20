'use strict';

angular.module('coreApp')
    .factory('CustomerOrder', function ($resource, DateUtils) {
        return $resource('api/customerOrders/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.placedOn = DateUtils.convertDateTimeFromServer(data.placedOn);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
