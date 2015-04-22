'use strict';

<<<<<<< HEAD
angular.module('schubberApp')
=======
angular.module('mightymerceApp')
>>>>>>> neues domainmodell
    .factory('Order', function ($resource) {
        return $resource('api/orders/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
<<<<<<< HEAD
                    var dateFrom = data.date.split("-");
                    data.date = new Date(new Date(dateFrom[0], dateFrom[1] - 1, dateFrom[2]));
                    return data;
                }
            }
=======
                    return data;
                }
            },
            'update': { method:'PUT' }
>>>>>>> neues domainmodell
        });
    });
