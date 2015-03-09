'use strict';

angular.module('studentshelpcenterApp')
    .factory('AnswerVote', function ($resource) {
        return $resource('api/answers/:id/votes/:voteId', {id: '@id', voteId: '@voteId'}, {
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
