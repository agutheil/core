'use strict';

angular.module('mightymerceApp')
    .factory('ChannelPost', function ($resource) {
        return $resource('api/channelPosts/:id', {}, {
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
