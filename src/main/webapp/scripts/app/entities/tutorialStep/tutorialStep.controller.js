'use strict';

angular.module('coreApp')
    .controller('TutorialStepController', function ($scope, TutorialStep) {
        $scope.tutorialSteps = [];
        $scope.loadAll = function() {
            TutorialStep.query(function(result) {
               $scope.tutorialSteps = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            TutorialStep.get({id: id}, function(result) {
                $scope.tutorialStep = result;
                $('#deleteTutorialStepConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            TutorialStep.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTutorialStepConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.tutorialStep = {step: null, completed: null, id: null};
        };
    });
