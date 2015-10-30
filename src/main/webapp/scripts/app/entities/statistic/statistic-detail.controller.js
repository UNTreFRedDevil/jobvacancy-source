'use strict';

angular.module('jobvacancyApp')
    .controller('StatisticDetailController', function ($scope, $rootScope, $stateParams, entity, Statistic) {
        $scope.statistic = entity;
        $scope.load = function (id) {
            Statistic.get({id: id}, function(result) {
                $scope.statistic = result;
            });
        };
        $rootScope.$on('jobvacancyApp:statisticUpdate', function(event, result) {
            $scope.statistic = result;
        });
    });
