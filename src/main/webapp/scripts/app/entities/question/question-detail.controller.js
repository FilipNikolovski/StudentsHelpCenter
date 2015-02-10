'use strict';

angular.module('studentshelpcenterApp')
    .controller('QuestionDetailController', function ($scope, $stateParams, Question, User) {
        $scope.question = {};
        $scope.load = function (id) {
            Question.get({id: id}, function(result) {
              $scope.question = result;
            });
        };
        $scope.load($stateParams.id);
    });
