'use strict';

angular.module('studentshelpcenterApp')
    .controller('AnswerVoteDetailController', function ($scope, $stateParams, AnswerVote) {
        $scope.answerVote = {};
        $scope.load = function (id) {
            AnswerVote.get({id: id}, function(result) {
              $scope.answerVote = result;
            });
        };
        $scope.load($stateParams.id);
    });
