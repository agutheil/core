'use strict';

angular.module('schubberApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
