'use strict';

angular.module('coreApp')
    .controller('LegalInfoDetailController', function ($scope, $rootScope, $stateParams, entity, LegalInfo, User) {
        $scope.legalInfo = entity;
        $scope.load = function (id) {
            LegalInfo.get({id: id}, function(result) {
                $scope.legalInfo = result;
            });
        };
        $rootScope.$on('coreApp:legalInfoUpdate', function(event, result) {
            $scope.legalInfo = result;
        });
    });
