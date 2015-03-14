'use strict';

angular.module('studentshelpcenterApp')
    .factory('Answer', function ($resource) {
        return $resource('api/questions/:id/answers/:answerId?size=:size&page=:page', {id: '@id', answerId: '@answerId', page: '@page', size: '@size'}, {
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
