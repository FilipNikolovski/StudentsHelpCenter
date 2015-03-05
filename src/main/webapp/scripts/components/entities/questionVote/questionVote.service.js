'use strict';

angular.module('studentshelpcenterApp')
    .factory('QuestionVote', function ($resource) {
        return $resource('api/questions/:id/votes/:voteId', {}, {
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
