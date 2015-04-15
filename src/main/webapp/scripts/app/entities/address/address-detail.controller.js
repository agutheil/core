'use strict';

angular.module('mightymerceApp')
    .controller('AddressDetailController', function ($scope, $stateParams, Address, Customer) {
        $scope.address = {};
        $scope.load = function (id) {
            Address.get({id: id}, function(result) {
              $scope.address = result;
            });
        };
        $scope.load($stateParams.id);
    });
