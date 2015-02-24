'use strict';

angular.module('studentshelpcenterApp')
    .controller('QuestionDetailsController', function ($scope, $stateParams, Question, Answer) {
        $scope.question = {};
        $scope.load = function (id) {
            Question.get({id: id}, function(result) {
                $scope.question = result;
            });
        };

        $scope.load($stateParams.id);

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
