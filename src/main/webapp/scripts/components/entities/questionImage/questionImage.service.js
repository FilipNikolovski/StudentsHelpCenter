'use strict';

angular.module('studentshelpcenterApp')
    .factory('QuestionImage', function ($resource) {
        return $resource('api/questionImages/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });
