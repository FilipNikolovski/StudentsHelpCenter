'use strict';

angular.module('studentshelpcenterApp')
    .controller('QuestionTagController', function ($scope, QuestionTag, Tag, Question) {
        $scope.questionTags = [];
        $scope.tags = Tag.query();
        $scope.questions = Question.query();
        $scope.loadAll = function() {
            QuestionTag.query(function(result) {
               $scope.questionTags = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            QuestionTag.save($scope.questionTag,
                function () {
                    $scope.loadAll();
                    $('#saveQuestionTagModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.questionTag = QuestionTag.get({id: id});
            $('#saveQuestionTagModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.questionTag = QuestionTag.get({id: id});
            $('#deleteQuestionTagConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            QuestionTag.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteQuestionTagConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.questionTag = {id: null};
        };
    });
