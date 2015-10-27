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

angular.module('coreApp')
    .factory('CustomerOrderByOrderStatus', function ($resource, DateUtils) {
        return $resource('api/customerOrderByStatus/{orderStatus}', {}, {
            'get': {
                method: 'GET',
                isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.placedOn = DateUtils.convertDateTimeFromServer(data.placedOn);
                    return data;
                }
            }
        });
    });

angular.module('coreApp')
    .factory('PerformanceStats', function ($resource) {
        return $resource('api/performanceStats', {}, {
            'get': {
                method: 'GET',
                isArray: false
            }
        });
    });
