'use strict';

angular.module('coreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('notification', {
                parent: 'entity',
                url: '/notifications',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.notification.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/notification/notifications.html',
                        controller: 'NotificationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('notification');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('notification.detail', {
                parent: 'entity',
                url: '/notification/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.notification.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/notification/notification-detail.html',
                        controller: 'NotificationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('notification');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Notification', function($stateParams, Notification) {
                        return Notification.get({id : $stateParams.id});
                    }]
                }
            })
            .state('notification.new', {
                parent: 'notification',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/notification/notification-dialog.html',
                        controller: 'NotificationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {title: null, purpose: null, emailSubject: null, emailBody: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('notification', null, { reload: true });
                    }, function() {
                        $state.go('notification');
                    })
                }]
            })
            .state('notification.edit', {
                parent: 'notification',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/notification/notification-dialog.html',
                        controller: 'NotificationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Notification', function(Notification) {
                                return Notification.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('notification', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
