/**
 * Created by smhumayun on 10/3/2015.
 */
'use strict';

angular.module('coreApp')
    .factory('Lookup', function ($resource) {
        return $resource('api/lookup/:lookupName', {}, {
            'get': {
                method: 'GET',
                isArray: true,
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });
