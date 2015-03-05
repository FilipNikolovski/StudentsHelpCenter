'use strict';

angular.module('studentshelpcenterApp')
    .factory('Answer', function ($resource) {
        return $resource('api/questions/:id/answers/:answerId', {}, {
            'query': { method: 'GET', isArray: true},
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
