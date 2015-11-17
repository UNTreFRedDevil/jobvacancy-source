'use strict';

angular.module('jobvacancyApp')
    .filter('jobOfferStatus', function () {
        return function (input) {
            var statuses = {
                'AVAILABLE': 'Available',
                'JOBVACANCY_HIRED': 'Satisfied with a JobVacancy candidate',
                'EXTERNAL_HIRED': 'Satisfied with an external candidate',
                'CANCELED': 'Canceled offer, no one was hired'
            };

            return statuses[input] || '';
        };
    });
