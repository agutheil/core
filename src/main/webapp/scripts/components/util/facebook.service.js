'use strict';

angular.module('coreApp')
    .service('Facebook', function($q) {
        var facebookToken = 'empty';
        this.watchLoginChange = function() {
          var _self = this;
          FB.Event.subscribe('auth.authResponseChange', function(response) {
            if (response.status === 'connected') {
              _self.facebookToken = response.authResponse.accessToken;
              console.log('facebookToken: '+ _self.facebookToken);
            } 
            else {
                _self.facebookToken = 'empty';
                console.log("not authorized");
            }
          });
        };
    });