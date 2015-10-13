'use strict';

angular.module('coreApp')
    .controller('MainController', function ($scope, Principal, TutorialStep) {
        $scope.tutorialSteps = TutorialStep.query();
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
/*
            if(Principal.isAuthenticated) {
                TutorialStep.get({user: $scope.account.id}, function(result) {
                    $scope.tutorialSteps = result;
                });
            }
*/
        });
    });
