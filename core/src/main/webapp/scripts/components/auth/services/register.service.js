'use strict';

angular.module('mightymerceApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


