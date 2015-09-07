'use strict';

angular.module('coreApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
