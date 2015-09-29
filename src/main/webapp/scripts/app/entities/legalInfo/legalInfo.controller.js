'use strict';

angular.module('coreApp')
    .controller('LegalInfoController', function ($scope, LegalInfo) {
        $scope.legalInfos = [];
        $scope.loadAll = function() {
            LegalInfo.query(function(result) {
               $scope.legalInfos = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            LegalInfo.get({id: id}, function(result) {
                $scope.legalInfo = result;
                $('#deleteLegalInfoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            LegalInfo.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteLegalInfoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.legalInfo = {title: null, purpose: null, pageTitle: null, pageText: null, id: null};
        };
    });
