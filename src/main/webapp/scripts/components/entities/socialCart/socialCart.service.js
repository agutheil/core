'use strict';

angular.module('mightymerceApp')
    .factory('SocialCart', function ($resource) {
        return $resource('api/socialCarts/:id', {}, {
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
