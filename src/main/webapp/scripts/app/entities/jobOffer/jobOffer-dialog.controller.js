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

            if(typeof $scope.jobOffer.endDate == 'undefined'){
                $scope.jobOffer.endDate = null;
            }
            if (validateDate($scope.jobOffer.startDate) === true && validateDate($scope.jobOffer.endDate) === true){
                $scope.editForm.startDate.$invalid=false;
                $scope.editForm.startDate.$valid=true;
                $scope.editForm.startDate.$error=false;
                $scope.editForm.endDate.$invalid=false;
                $scope.editForm.endDate.$valid=true;
                $scope.editForm.endDate.$error=false;
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
                if(validateDate($scope.jobOffer.startDate) === false){
                    $scope.editForm.startDate.$invalid=true;
                    $scope.editForm.startDate.$valid=false;
                    $scope.editForm.startDate.$error=true;
                }
                if(validateDate($scope.jobOffer.endDate) === false){
                    $scope.editForm.endDate.$invalid=true;
                    $scope.editForm.endDate.$valid=false;
                    $scope.editForm.endDate.$error=true;
                }
                document.getElementById("save-button").disabled = false;
            }

        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
       $scope.today = function () {
            if ($scope.jobOffer.startDate == null){
                $scope.jobOffer.startDate = new Date();
            }

            if ($scope.jobOffer.endDate == null){
                $scope.jobOffer.endDate = new Date();
            }

        };
       $scope.today();
       $scope.openDatePickerStartdDate = function($event) {
            $scope.statusDatePickerStartdDate.opened = true;
       };

       $scope.openDatePickerEndDate = function($event) {
            $scope.statusDatePickerEndDate.opened = true;
       };

       $scope.dateOptions = {
            formatYear: 'yy'
       };

       $scope.statusDatePickerStartdDate = {
            opened: false
       };

       $scope.statusDatePickerEndDate = {
           opened: false
       };

       var validateDate = function(dateToValidate) {
           console.log("Date: " + dateToValidate);
           if(dateToValidate === null){
               return true;
           }
           var today = new Date();
           today.setHours(0);
           today.setMilliseconds(0);
           today.setMinutes(0);
           today.setSeconds(0);
           var inputDate = new Date(dateToValidate);
           inputDate.setHours(0);
           inputDate.setMilliseconds(0);
           inputDate.setMinutes(0);
           inputDate.setSeconds(0);
           var compare = inputDate.getTime() >= today.getTime();
           console.log("Comparo la fecha de hoy: " +today + " con la fecha ingresada: "+ inputDate +" y el resultado es: "+ compare);
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
