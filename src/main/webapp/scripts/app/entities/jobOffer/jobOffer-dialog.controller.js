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
            if (compareDates() === true){
                $scope.editForm.startDate.$invalid=false;
                $scope.editForm.startDate.$valid=true;
                $scope.editForm.startDate.$error=false;
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
            }
            else{
                $scope.editForm.startDate.$invalid=true;
                $scope.editForm.startDate.$valid=false;
                $scope.editForm.startDate.$error=true;
                document.getElementById("save-button").disabled = false;
            }

        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
       $scope.today = function () {
            $scope.jobOffer.startDate = new Date();
        };
        $scope.today();
        $scope.open = function($event) {
            $scope.status.opened = true;
        };

        $scope.dateOptions = {
            formatYear: 'yy'
        };

        $scope.status = {
            opened: false
        };

        var compareDates = function() {

            if($scope.jobOffer.startDate === null){
                return true;
            }
            var today = new Date();
            today.setHours(0);
            today.setMilliseconds(0);
            today.setMinutes(0);
            today.setSeconds(0);
            var inputDate = new Date($scope.jobOffer.startDate);
            inputDate.setHours(0);
            inputDate.setMilliseconds(0);
            inputDate.setMinutes(0);
            inputDate.setSeconds(0);
            var compare = inputDate.getTime() >= today.getTime();
            return compare;
        };



        var tomorrow = new Date();
          tomorrow.setDate(tomorrow.getDate() + 1);
          var afterTomorrow = new Date();
          afterTomorrow.setDate(tomorrow.getDate() + 2);
          $scope.events =
            [
              {
                date: tomorrow,
                status: 'full'
              },
              {
                date: afterTomorrow,
                status: 'partially'
              }
            ];

          $scope.getDayClass = function(date, mode) {
            if (mode === 'day') {
              var dayToCheck = new Date(date).setHours(0,0,0,0);

              for (var i=0;i<$scope.events.length;i++){
                var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

                if (dayToCheck === currentDay) {
                  return $scope.events[i].status;
                }
              }
            }

            return '';
          };
}]);
