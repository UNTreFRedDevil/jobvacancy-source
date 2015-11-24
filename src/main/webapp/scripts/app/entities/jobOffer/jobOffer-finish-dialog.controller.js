'use strict';

angular.module('jobvacancyApp').controller('JobOfferFinishDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'JobOfferActions',
        function($scope, $stateParams, $modalInstance, entity, JobOfferActions) {

        $scope.jobOffer = entity;
        $scope.status = '';

        var onFinishFinished = function (result) {
            $scope.$emit('jobvacancyApp:jobOfferUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            $scope.jobOffer.status = $scope.status;
            JobOfferActions.finish($scope.jobOffer, onFinishFinished);
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };

}]);
