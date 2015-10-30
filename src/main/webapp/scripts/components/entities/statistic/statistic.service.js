'use strict';

angular.module('jobvacancyApp')
    .factory('Statistic', function ($resource, DateUtils) {
        return $resource('api/statistics/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
