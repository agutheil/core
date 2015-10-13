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
            'update': { method:'PUT' },
            'save': {
                method:'POST',

                /**
                 * Do not remove following function although it seems ike it is not doing any thing but if you
                 * remove it, the error alert message appearing on the UI will loose its error message and display
                 * empty brackets {} instead. I have not looked into the details yet as to why it is happening like
                 * that, will dug deeper into later on to find out the root cause
                 */
                transformResponse: function (data) {
                    return data;
                }
            }
        });
    });

angular.module('coreApp')
    .factory('ChannelPostsByProductIds', function ($resource) {
        return $resource('api/channelPostsByProductIds', {}, {
            'get': { method:'GET', isArray:false }
        });
    });
