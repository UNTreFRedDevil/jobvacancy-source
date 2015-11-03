'use strict';

angular.module('jobvacancyApp')
    .controller('StatisticController', function ($scope, Statistic) {
        $scope.statistics = [];
        $scope.loadAll = function() {
            Statistic.query(function(result) {
               $scope.statistics = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Statistic.get({id: id}, function(result) {
                $scope.statistic = result;
                $('#deleteStatisticConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Statistic.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteStatisticConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.statistic = {
                metric: null,
                value: null,
                id: null
            };
        };
    });
