'use strict';

angular.module('studentshelpcenterApp')
    .controller('QuestionVoteController', function ($scope, QuestionVote, User, Question) {
        $scope.questionVotes = [];
        $scope.users = User.query();
        $scope.questions = Question.query();
        $scope.loadAll = function() {
            QuestionVote.query(function(result) {
               $scope.questionVotes = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            QuestionVote.save($scope.questionVote,
                function () {
                    $scope.loadAll();
                    $('#saveQuestionVoteModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.questionVote = QuestionVote.get({id: id});
            $('#saveQuestionVoteModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.questionVote = QuestionVote.get({id: id});
            $('#deleteQuestionVoteConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            QuestionVote.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteQuestionVoteConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.questionVote = {vote: null, id: null};
        };
    });
