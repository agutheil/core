'use strict';

angular.module('coreApp').controller('SocialOrderDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'SocialOrder', 'Article', 'Address', 'User',
        function($scope, $stateParams, $modalInstance, entity, SocialOrder, Article, Address, User) {

        $scope.socialOrder = entity;
        $scope.articles = Article.query();
        $scope.addresss = Address.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            SocialOrder.get({id : id}, function(result) {
                $scope.socialOrder = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('coreApp:socialOrderUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.socialOrder.id != null) {
                SocialOrder.update($scope.socialOrder, onSaveFinished);
            } else {
                SocialOrder.save($scope.socialOrder, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
