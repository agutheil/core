'use strict';

<<<<<<< HEAD
angular.module('schubberApp')
=======
angular.module('mightymerceApp')
>>>>>>> neues domainmodell
    .factory('Channel', function ($resource) {
        return $resource('api/channels/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
<<<<<<< HEAD
            }
=======
            },
            'update': { method:'PUT' }
>>>>>>> neues domainmodell
        });
    });
