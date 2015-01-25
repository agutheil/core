function Hello($scope, $http) {
    $http.get('http://localhost:8080/products/30').
        success(function(data) {
            $scope.product = data;
        });
}