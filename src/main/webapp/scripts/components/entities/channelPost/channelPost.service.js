'use strict';

angular.module('coreApp')
    .factory('ChannelPost', function ($resource, DateUtils) {
        return $resource('api/channelPosts/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.publicationDate = DateUtils.convertDateTimeFromServer(data.publicationDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
