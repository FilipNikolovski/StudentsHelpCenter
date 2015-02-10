'use strict';

angular.module('studentshelpcenterApp')
    .factory('AnswerVote', function ($resource) {
        return $resource('api/answerVotes/:id', {}, {
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
