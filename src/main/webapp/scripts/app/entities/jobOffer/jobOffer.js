'use strict';

angular.module('jobvacancyApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('jobOffer', {
                parent: 'entity',
                url: '/jobOffers',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'JobOffers'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jobOffer/jobOffers.html',
                        controller: 'JobOfferController'
                    }
                },
                resolve: {
                }
            })
            .state('jobOffer.detail', {
                parent: 'entity',
                url: '/jobOffer/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'JobOffer'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/jobOffer/jobOffer-detail.html',
                        controller: 'JobOfferDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'JobOffer', function($stateParams, JobOffer) {
                        return JobOffer.get({id : $stateParams.id});
                    }]
                }
            })
            .state('jobOffer.new', {
                parent: 'jobOffer',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/jobOffer/jobOffer-dialog.html',
                        controller: 'JobOfferDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {title: null, location: null, description: null, status: 'AVAILABLE', id: null};
                            },
                            isACopy: function(){return false;}
                        }
                    }).result.then(function(result) {
                        $state.go('jobOffer', null, { reload: true });
                    }, function() {
                        $state.go('jobOffer');
                    });
                }]
            })
            .state('jobOffer.copy', {
                parent: 'jobOffer',
                url: '/{id}/copy',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/jobOffer/jobOffer-dialog.html',
                        controller: 'JobOfferDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['JobOffer', function(JobOffer) {
                                return JobOffer.get({id : $stateParams.id});
                            }],
                            isACopy: function(){return true;}
                        }
                    }).result.then(function(result) {
                        $state.go('jobOffer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            })
            .state('jobOffer.edit', {
                parent: 'jobOffer',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/jobOffer/jobOffer-dialog.html',
                        controller: 'JobOfferDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['JobOffer', function(JobOffer) {
                                return JobOffer.get({id : $stateParams.id});
                            }],
                            isACopy: function(){return false;}
                        }
                    }).result.then(function(result) {
                        $state.go('jobOffer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    });
