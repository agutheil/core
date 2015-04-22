'use strict';

angular.module('mightymerceApp')
    .factory('CustomerChannel', function ($resource) {
        return $resource('api/customerChannels/:id', {}, {
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
