'use strict';

angular.module('jobvacancyApp').controller('StatisticDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Statistic',
        function($scope, $stateParams, $modalInstance, entity, Statistic) {

        $scope.statistic = entity;
        $scope.load = function(id) {
            Statistic.get({id : id}, function(result) {
                $scope.statistic = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jobvacancyApp:statisticUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.statistic.id != null) {
                Statistic.update($scope.statistic, onSaveFinished);
            } else {
                Statistic.save($scope.statistic, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
