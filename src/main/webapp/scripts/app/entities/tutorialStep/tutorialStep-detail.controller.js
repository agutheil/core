'use strict';

angular.module('coreApp')
    .controller('TutorialStepDetailController', function ($scope, $rootScope, $stateParams, entity, TutorialStep, User) {
        $scope.tutorialStep = entity;
        $scope.load = function (id) {
            TutorialStep.get({id: id}, function(result) {
                $scope.tutorialStep = result;
            });
        };
        $rootScope.$on('coreApp:tutorialStepUpdate', function(event, result) {
            $scope.tutorialStep = result;
        });
    });
