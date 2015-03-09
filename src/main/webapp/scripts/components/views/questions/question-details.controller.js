'use strict';

angular.module('studentshelpcenterApp')
    .filter('to_trusted', ['$sce', function ($sce) {
        return function (text) {
            return $sce.trustAsHtml(text);
        };
    }])
    .controller('QuestionDetailsController', function ($scope, $stateParams, Question, Answer, QuestionImage, QuestionVote, AnswerVote, Account) {

        $scope.question = {};
        $scope.vote = {};
        $scope.question.answers = [];
        $scope.question.images = [];

        $scope.load = function (id) {
            Question.get({id: id}, function (result) {
                $scope.question = result;
            });

            Answer.query({id: id}, function (answers) {
                $scope.question.answers = answers;
            });

            QuestionImage.query({id: id}, function (images) {
                $scope.question.images = images;
            });
        };

        $scope.upvoteQuestion = function (id) {
            $scope.vote.vote = 1;
            QuestionVote.save({id: id}, $scope.vote,
                function () {
                    $scope.load($stateParams.id);
                });
        };

        $scope.downvoteQuestion = function (id) {
            $scope.vote.vote = -1;
            QuestionVote.save({id: id}, $scope.vote,
                function () {
                    $scope.load($stateParams.id);
                });
        };

        $scope.showImage = function (imageName) {
            $scope.imageName = imageName;
            console.log(imageName);
            $('#showImage').modal('show');

        };

        $scope.load($stateParams.id);

        $scope.create = function () {
            $scope.answer.datePosted = new Date();
            Answer.save({id: $stateParams.id}, $scope.answer,
                function () {
                    $scope.load($stateParams.id);
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
            $scope.imageName = null;
        };
    });
