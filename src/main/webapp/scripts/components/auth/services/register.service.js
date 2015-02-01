'use strict';

angular.module('schubberApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


