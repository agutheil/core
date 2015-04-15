'use strict';

<<<<<<< HEAD
angular.module('schubberApp')
    .controller('CustomerDetailController', function ($scope, $stateParams, Customer) {
=======
angular.module('mightymerceApp')
    .controller('CustomerDetailController', function ($scope, $stateParams, Customer, Address) {
>>>>>>> customer und adresse
        $scope.customer = {};
        $scope.load = function (id) {
            Customer.get({id: id}, function(result) {
              $scope.customer = result;
            });
        };
        $scope.load($stateParams.id);
    });
