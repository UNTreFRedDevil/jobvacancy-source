'use strict';

angular.module('jobvacancyApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('statistic', {
                parent: 'entity',
                url: '/statistics',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Statistics'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/statistic/statistics.html',
                        controller: 'StatisticController'
                    }
                },
                resolve: {
                }
            })
            .state('statistic.detail', {
                parent: 'entity',
                url: '/statistic/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Statistic'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/statistic/statistic-detail.html',
                        controller: 'StatisticDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Statistic', function($stateParams, Statistic) {
                        return Statistic.get({id : $stateParams.id});
                    }]
                }
            })
            .state('statistic.new', {
                parent: 'statistic',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/statistic/statistic-dialog.html',
                        controller: 'StatisticDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    metric: null,
                                    value: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('statistic', null, { reload: true });
                    }, function() {
                        $state.go('statistic');
                    })
                }]
            })
            .state('statistic.edit', {
                parent: 'statistic',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/statistic/statistic-dialog.html',
                        controller: 'StatisticDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Statistic', function(Statistic) {
                                return Statistic.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('statistic', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
