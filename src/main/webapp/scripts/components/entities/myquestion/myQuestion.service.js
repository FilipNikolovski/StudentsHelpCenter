'use strict';
angular.module('studentshelpcenterApp')
    .factory('MyQuestion', function ($resource) {
        return $resource('api/myQuestions/?size=:size&page=:page', {page: '@page', size: '@size'}, {
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
