 'use strict';

angular.module('coreApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-coreApp-alert');
                if (angular.isString(alertKey)) {
                    var alertType = response.headers('X-coreApp-alert-type');
                    if(angular.isString(alertType) && alertType == 'warning') {
                        AlertService.warning(alertKey, null);
                    } else if(angular.isString(alertType) && alertType == 'error') {
                        AlertService.error(alertKey, null);
                    } else {
                        AlertService.success(alertKey, { param : response.headers('X-coreApp-params')});
                    }
                }
                return response;
            },
        };
    });
