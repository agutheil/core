'use strict';

angular.module('coreApp').controller('ArticleDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Article', 'User',
        function($scope, $stateParams, $modalInstance, entity, Article, User) {

        $scope.article = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Article.get({id : id}, function(result) {
                $scope.article = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('coreApp:articleUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.article.id != null) {
                Article.update($scope.article, onSaveFinished);
            } else {
                Article.save($scope.article, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
