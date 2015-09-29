'use strict';

angular.module('coreApp')
    .controller('NotificationDetailController', function ($scope, $rootScope, $stateParams, entity, Notification, User) {
        $scope.notification = entity;
        $scope.load = function (id) {
            Notification.get({id: id}, function(result) {
                $scope.notification = result;
            });
        };
        $rootScope.$on('coreApp:notificationUpdate', function(event, result) {
            $scope.notification = result;
        });
    });
