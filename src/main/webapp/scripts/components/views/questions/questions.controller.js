'use strict';

angular.module('studentshelpcenterApp')
    .controller('QuestionsController', function ($scope, Question, Principal, QuestionVote) {
        Principal.identity().then(function (account) {
            $scope.account = account;
        });

        $scope.questions = [];
        $scope.loadAll = function() {
            Question.query(function(result) {
                $scope.questions = result;
            });
        };
        $scope.loadAll();

        $scope.clear = function () {
            $scope.question = {title: null, description: null, datePosted: null, solved: null, id: null};
        };
    });
