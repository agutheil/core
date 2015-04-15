'use strict';

<<<<<<< HEAD
angular.module('schubberApp')
=======
angular.module('mightymerceApp')
>>>>>>> jhipster
    .factory('Password', function ($resource) {
        return $resource('api/account/change_password', {}, {
        });
    });
