'use strict';

angular.module('studentshelpcenterApp')
    .controller('AnswerController', function ($scope, Answer, User, Question) {
        $scope.answers = [];
        $scope.users = User.query();
        $scope.questions = Question.query();
        $scope.loadAll = function() {
            Answer.query(function(result) {
               $scope.answers = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Answer.save($scope.answer,
                function () {
                    $scope.loadAll();
                    $('#saveAnswerModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.answer = Answer.get({id: id});
            $('#saveAnswerModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.answer = Answer.get({id: id});
            $('#deleteAnswerConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            Answer.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAnswerConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.answer = {answerText: null, datePosted: null, id: null};
        };
    });
