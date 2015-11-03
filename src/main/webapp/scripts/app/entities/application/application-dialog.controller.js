'use strict';

angular.module('jobvacancyApp').controller('ApplicationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'JobOffer','Application',
        function($scope, $stateParams, $modalInstance, entity, JobOffer,Application) {

        $scope.jobApplication = entity;

        var onSaveFinished = function (result) {
            $scope.$emit('jobvacancyApp:jobOfferUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            Application.save($scope.jobApplication, onSaveFinished);
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };

}]);
