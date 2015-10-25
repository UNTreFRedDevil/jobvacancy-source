'use strict';

angular.module('jobvacancyApp').controller('JobOfferDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'JobOffer', 'User','isACopy',
        function($scope, $stateParams, $modalInstance, entity, JobOffer, User, isACopy) {

        $scope.jobOffer = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            JobOffer.get({id : id}, function(result) {
                $scope.jobOffer = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jobvacancyApp:jobOfferUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.jobOffer.id != null) {
                if(isACopy === true){
                    $scope.jobOffer.id = null;
                    JobOffer.save($scope.jobOffer, onSaveFinished);
                }
                else{
                    JobOffer.update($scope.jobOffer, onSaveFinished);
                }

            } else {
                JobOffer.save($scope.jobOffer, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
