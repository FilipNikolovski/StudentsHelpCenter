'use strict';

angular.module('studentshelpcenterApp')
    .factory('QuestionTag', function ($resource) {
        return $resource('api/questionTags/:id', {}, {
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
