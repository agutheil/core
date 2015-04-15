'use strict';

<<<<<<< HEAD
angular.module('schubberApp')
=======
angular.module('mightymerceApp')
>>>>>>> jhipster
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


