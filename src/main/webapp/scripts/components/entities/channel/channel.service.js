'use strict';

angular.module('schubberApp')
    .factory('Channel', function ($resource) {
        return $resource('api/channels/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });
