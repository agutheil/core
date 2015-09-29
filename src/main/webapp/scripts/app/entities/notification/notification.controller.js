'use strict';

angular.module('coreApp')
    .controller('NotificationController', function ($scope, Notification) {
        $scope.notifications = [];
        $scope.loadAll = function() {
            Notification.query(function(result) {
               $scope.notifications = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Notification.get({id: id}, function(result) {
                $scope.notification = result;
                $('#deleteNotificationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Notification.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteNotificationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.notification = {title: null, purpose: null, emailSubject: null, emailBody: null, id: null};
        };
    });
