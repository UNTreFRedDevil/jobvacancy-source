'use strict';

angular.module('jobvacancyApp')
    .factory('JobOffer', function ($resource, DateUtils) {
        return $resource('api/jobOffers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.startDate =  DateUtils.convertLocaleDateFromServer(data.startDate);
                    data.endDate =  DateUtils.convertLocaleDateFromServer(data.endDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    })
    .factory('JobOfferActions', function ($resource) {
        return $resource('api/finish-job-offer/:id', {}, {
            'finish': { method: 'PUT' },
        });
    })
    .factory('Offer', function ($resource, DateUtils) {
        return $resource('api/offers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
        });
    });
