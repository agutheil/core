'use strict';

angular.module('coreApp').controller('NotificationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Notification', 'User',
        function($scope, $stateParams, $modalInstance, entity, Notification, User) {

        $scope.notification = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Notification.get({id : id}, function(result) {
                $scope.notification = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('coreApp:notificationUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.notification.id != null) {
                Notification.update($scope.notification, onSaveFinished);
            } else {
                Notification.save($scope.notification, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
