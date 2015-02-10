'use strict';

angular.module('studentshelpcenterApp')
    .controller('QuestionImageController', function ($scope, QuestionImage, Question) {
        $scope.questionImages = [];
        $scope.questions = Question.query();
        $scope.loadAll = function() {
            QuestionImage.query(function(result) {
               $scope.questionImages = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            QuestionImage.save($scope.questionImage,
                function () {
                    $scope.loadAll();
                    $('#saveQuestionImageModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.questionImage = QuestionImage.get({id: id});
            $('#saveQuestionImageModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.questionImage = QuestionImage.get({id: id});
            $('#deleteQuestionImageConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            QuestionImage.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteQuestionImageConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.questionImage = {imageName: null, id: null};
        };
    });
