'use strict';

angular.module('studentshelpcenterApp')
    .controller('AnswerVoteController', function ($scope, AnswerVote, Answer, User) {
        $scope.answerVotes = [];
        $scope.answers = Answer.query();
        $scope.users = User.query();
        $scope.loadAll = function() {
            AnswerVote.query(function(result) {
               $scope.answerVotes = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            AnswerVote.save($scope.answerVote,
                function () {
                    $scope.loadAll();
                    $('#saveAnswerVoteModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.answerVote = AnswerVote.get({id: id});
            $('#saveAnswerVoteModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.answerVote = AnswerVote.get({id: id});
            $('#deleteAnswerVoteConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            AnswerVote.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAnswerVoteConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.answerVote = {vote: null, id: null};
        };
    });
