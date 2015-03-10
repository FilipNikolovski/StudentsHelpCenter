'use strict';

angular.module('studentshelpcenterApp')
    .filter('to_trusted', ['$sce', function ($sce) {
        return function (text) {
            return $sce.trustAsHtml(text);
        };
    }])
    .controller('QuestionDetailsController', function ($scope, $stateParams, Question, Answer, QuestionImage, Account, QuestionVote, AnswerVote, Principal, $window) {
        Principal.identity().then(function (account) {
            $scope.account = account;
        });

        $scope.question = {};
        $scope.vote = {};
        $scope.question.answers = [];
        $scope.question.images = [];

        $scope.deleteAnswer = -1;
        $scope.updateAnswer = {};

        $scope.deleteQuestion={};

        $scope.load = function (id) {
            Question.get({id: id}).$promise.then(function (result) {
                $scope.question = result;

                Answer.query({id: id}).$promise.then(function (answers) {
                    $scope.question.answers = answers;
                });

                QuestionImage.query({id: id}).$promise.then(function (images) {
                    $scope.question.images = images;
                });
            });
        };

        $scope.load($stateParams.id);

        $scope.upvoteQuestion = function (id) {
            $scope.vote.vote = 1;
            QuestionVote.save({id: id}, $scope.vote,
                function () {
                    $scope.load($stateParams.id);
                    $scope.clear();
                });
        };

        $scope.downvoteQuestion = function (id) {
            $scope.vote.vote = -1;
            QuestionVote.save({id: id}, $scope.vote,
                function () {
                    $scope.load($stateParams.id);
                    $scope.clear();
                });
        };

        $scope.showImage = function (imageName) {
            $scope.imageName = imageName;
        };

        $scope.upVoteAnswer = function (answer) {
            $scope.vote.answer = answer;
            $scope.vote.vote = 1;
            console.log($scope.vote);
            AnswerVote.save({id: $stateParams.id}, $scope.vote, function () {
                $scope.load($stateParams.id);
                $scope.clear();
            });
        };

        $scope.downVoteAnswer = function (answer) {
            $scope.vote.answer = answer;
            $scope.vote.vote = -1;
            AnswerVote.save({id: $stateParams.id}, $scope.vote, function () {
                $scope.load($stateParams.id);
                $scope.clear();
            });
        };

        $scope.showImage = function (imageName) {
            $scope.imageName = imageName;
            console.log(imageName);
            $('#showImage').modal('show');
        };

        $scope.create = function () {
            $scope.updateAnswer.datePosted = new Date();
            $scope.updateAnswer.question = $scope.question;
            $scope.updateAnswer.user = $scope.account;
            Answer.save({id: $stateParams.id}, $scope.updateAnswer,
                function () {
                    $scope.load($stateParams.id);
                    $scope.clear();
                });
        };

        $scope.update = function (answer) {
            $scope.updateAnswer = Answer.get({id: $scope.question.id, answerId: answer.id});
        };

        $scope.questionDelete=function(question)
        {
            $scope.deleteQuestion = question;
            $('#deleteQuestionConfirmation').modal('show');
        };

        $scope.confirmQuestionDelete = function () {
            Question.delete({id: deleteQuestion.id},
                function () {
                    $scope.load($stateParams.id);
                    $('#deleteQuestionConfirmation').modal('hide');
                    $scope.clear();
                    $window.location.href = '/#/questions';
                });
        };

        $scope.deleteAnswer = function (answer) {
            $scope.deleteAnswer = answer;
            $('#deleteAnswerConfirmation').modal('show');
        };

        $scope.confirmDelete = function () {
            Answer.delete({id: $stateParams.id, answerId: $scope.deleteAnswer.id},
                function () {
                    $scope.load($stateParams.id);
                    $('#deleteAnswerConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.imageName = null;
            $scope.deleteAnswer = {};
            $scope.updateAnswer = {};
            $scope.vote = {};
            $scope.deleteQuestion={};
        };

    });
