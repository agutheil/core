'use strict';

angular.module('schubberApp')
    .controller('ProductController', function ($scope, Product) {
        $scope.products = [];
        $scope.loadAll = function() {
            Product.query(function(result) {
               $scope.products = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Product.save($scope.product,
                function () {
                    $scope.loadAll();
                    $('#saveProductModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.product = Product.get({id: id});
            $('#saveProductModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.product = Product.get({id: id});
            $('#deleteProductConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            Product.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteProductConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.product = {title: null, description: null, price: null, currency: null, vatIncluded: null, id: null};
        };
    });
