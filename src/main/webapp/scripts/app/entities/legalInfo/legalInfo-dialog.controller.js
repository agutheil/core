'use strict';

angular.module('coreApp').controller('LegalInfoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'LegalInfo', 'User',
        function($scope, $stateParams, $modalInstance, entity, LegalInfo, User) {

        $scope.legalInfo = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            LegalInfo.get({id : id}, function(result) {
                $scope.legalInfo = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('coreApp:legalInfoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.legalInfo.id != null) {
                LegalInfo.update($scope.legalInfo, onSaveFinished);
            } else {
                LegalInfo.save($scope.legalInfo, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
