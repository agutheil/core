'use strict';

angular.module('coreApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('tutorialStep', {
                parent: 'entity',
                url: '/tutorialSteps',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.tutorialStep.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tutorialStep/tutorialSteps.html',
                        controller: 'TutorialStepController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tutorialStep');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('tutorialStep.detail', {
                parent: 'entity',
                url: '/tutorialStep/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'coreApp.tutorialStep.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tutorialStep/tutorialStep-detail.html',
                        controller: 'TutorialStepDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tutorialStep');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TutorialStep', function($stateParams, TutorialStep) {
                        return TutorialStep.get({id : $stateParams.id});
                    }]
                }
            })
            .state('tutorialStep.new', {
                parent: 'tutorialStep',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/tutorialStep/tutorialStep-dialog.html',
                        controller: 'TutorialStepDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {step: null, completed: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('tutorialStep', null, { reload: true });
                    }, function() {
                        $state.go('tutorialStep');
                    })
                }]
            })
            .state('tutorialStep.edit', {
                parent: 'tutorialStep',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/tutorialStep/tutorialStep-dialog.html',
                        controller: 'TutorialStepDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TutorialStep', function(TutorialStep) {
                                return TutorialStep.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tutorialStep', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
