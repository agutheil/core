'use strict';

angular.module('coreApp')
    .factory('TutorialStep', function ($resource, DateUtils) {
        return $resource('api/tutorialSteps/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
