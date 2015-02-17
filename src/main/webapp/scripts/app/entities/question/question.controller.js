'use strict';

angular.module('studentshelpcenterApp')
    .controller('QuestionController', function ($scope, Question) {
        $scope.questions = [];
        $scope.loadAll = function() {
            Question.query(function(result) {
               $scope.questions = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Question.save($scope.question,
                function () {
                    $scope.loadAll();
                    $('#saveQuestionModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.question = Question.get({id: id});
            $('#saveQuestionModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.question = Question.get({id: id});
            $('#deleteQuestionConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            Question.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteQuestionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.question = {title: null, description: null, datePosted: null, solved: null, id: null};
        };
    });
