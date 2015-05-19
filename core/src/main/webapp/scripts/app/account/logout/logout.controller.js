'use strict';

angular.module('mightymerceApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
