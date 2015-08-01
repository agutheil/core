'use strict';

angular.module('mightymerceApp')
    .service('Facebook', function($q) {
        var facebookToken = 'empty';
        this.getMyLastName = function() {
            var deferred = $q.defer();
            FB.api('/me', {
                fields: 'last_name'
            }, function(response) {
                console.log('FB-Response: '+ response);
                if (!response || response.error) {
                    deferred.reject('Error occured');
                } else {
                    deferred.resolve(response);
                }
            });
            return deferred.promise;
        };
        this.watchLoginChange = function() {
          var _self = this;
          FB.Event.subscribe('auth.authResponseChange', function(response) {
            if (response.status === 'connected') {
              console.log('accessToken: '+response.authResponse.accessToken);
              console.log('expiresIn: '+response.authResponse.expiresIn);
              console.log('signedRequest: '+response.authResponse.signedRequest);
              console.log('userID: '+response.authResponse.userID);
              facebookToken = response.authResponse.accessToken;
              console.log('facebookToken: '+facebookToken);
              //TODO: watch aus controller, dann aendern der infos im Frontend
            } 
            else {
                console.log("not authorized");
            }
          });
        };
    });