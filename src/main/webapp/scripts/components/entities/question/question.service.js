'use strict';

angular.module('studentshelpcenterApp')
    .factory('Question', function ($resource) {
        return $resource('api/questions/:id?size=:size&page=:page', {id: '@id', page: '@page', size: '@size'}, {
            'query': { method: 'GET', isArray: false},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.datePosted = new Date(data.datePosted);
                    return data;
                }
            }
        });
    });
