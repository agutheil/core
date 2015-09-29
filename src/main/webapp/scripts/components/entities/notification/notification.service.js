'use strict';

angular.module('coreApp')
    .factory('Notification', function ($resource, DateUtils) {
        return $resource('api/notifications/:id', {}, {
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
