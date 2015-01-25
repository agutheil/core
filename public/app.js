function productController($scope, $http) {
    $http.get('/products').
        success(function(data) {
            $scope.products = data;
        });
    $scope.id = '';
    $scope.title = '';
    $scope.price = '';
    $scope.currency = '';
    $scope.stock = '';

    $scope.edit = true;
    $scope.error = false;
    $scope.incomplete = false;

    $scope.editProduct = function(id) {
        if (id == 'new') {
            $scope.edit = true;
            $scope.incomplete = true;
            $scope.id = '';
            $scope.title = '';
            $scope.price = '';
            $scope.currency = '';
            $scope.stock = '';
        } else {
            $scope.edit = false;
            $scope.id = $scope.products[id].id;
            $scope.title = $scope.products[id].title;
            $scope.price = $scope.products[id].price;
            $scope.currency = $scope.products[id].currency;
            $scope.stock = $scope.products[id].stock;
        }
    };

    $scope.$watch('price',function() {$scope.test();});
    $scope.$watch('currency',function() {$scope.test();});
    $scope.$watch('id', function() {$scope.test();});
    $scope.$watch('title', function() {$scope.test();});
    $scope.test = function() {
        $scope.incomplete = false;
        if ($scope.edit && (!$scope.id.length || !$scope.title.length|| !$scope.price.length || !$scope.currency.length)) {
            $scope.incomplete = true;
        }
    };

}