'use strict';

angular.module('coreApp').controller('TutorialStepDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TutorialStep', 'User',
        function($scope, $stateParams, $modalInstance, entity, TutorialStep, User) {

        $scope.tutorialStep = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            TutorialStep.get({id : id}, function(result) {
                $scope.tutorialStep = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('coreApp:tutorialStepUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.tutorialStep.id != null) {
                TutorialStep.update($scope.tutorialStep, onSaveFinished);
            } else {
                TutorialStep.save($scope.tutorialStep, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
