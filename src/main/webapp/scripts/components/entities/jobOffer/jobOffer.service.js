'use strict';

angular.module('jobvacancyApp')
    .factory('JobOffer', function ($resource, DateUtils) {
        return $resource('api/jobOffers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    console.log (JSON.stringify(data.startDate));
                    //data.startDate = new Date(data.startDate);
                    data.startDate =  DateUtils.convertLocaleDateFromServer(data.startDate);
                    //data.endDate = new Date(data.endDate);
                    data.endDate =  DateUtils.convertLocaleDateFromServer(data.endDate);
                    console.log (JSON.stringify(data.startDate));
                    return data;
                }
            },
            'update': { method:'PUT' }
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
