'use strict';

describe('Controllers Tests ', function () {

<<<<<<< HEAD
    beforeEach(module('schubberApp'));
=======
    beforeEach(module('mightymerceApp'));
>>>>>>> jhipster

    describe('LoginController', function () {
        var $scope;


        beforeEach(inject(function ($rootScope, $controller) {
            $scope = $rootScope.$new();
            $controller('LoginController', {$scope: $scope});
        }));

        it('should set remember Me', function () {
            expect($scope.rememberMe).toBeTruthy();
        });
    });
});
