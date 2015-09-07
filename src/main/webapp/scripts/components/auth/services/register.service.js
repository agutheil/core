'use strict';

angular.module('coreApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


