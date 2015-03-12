'use strict';

angular.module('studentshelpcenterApp')
    .filter('to_trusted', ['$sce', function ($sce) {
        return function (text) {
            return $sce.trustAsHtml(text);
        };
    }])
    .controller('QuestionDetailsController', function ($scope, $state, $stateParams, Question, Answer, QuestionImage, Account, QuestionVote, AnswerVote, Principal) {
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
            console.log(id);
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

        $scope.upvoteAnswer = function (answerId) {
            $scope.vote.vote = 1;
            AnswerVote.save({id: answerId}, $scope.vote, function () {
                $scope.load($stateParams.id);
                $scope.clear();
            });
        };

        $scope.downvoteAnswer = function (answerId) {
            $scope.vote.vote = -1;
            AnswerVote.save({id: answerId}, $scope.vote, function () {
                $scope.load($stateParams.id);
                $scope.clear();
            });
        };

        $scope.showImage = function (imageName) {
            $scope.imageName = imageName;
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

        $scope.update = function (id) {
            Answer.get({id: $scope.question.id, answerId: id}).$promise.then(function (result) {
                $scope.updateAnswer = result;
            });
        };

        $scope.questionDelete=function(question)
        {
            $scope.deleteQuestion = question;
            $('#deleteQuestionConfirmation').modal('show');
        };

        $scope.confirmQuestionDelete = function () {
            Question.delete({id: $scope.deleteQuestion.id},
                function () {
                    $scope.load($stateParams.id);
                    $('#deleteQuestionConfirmation').modal('hide');
                    $scope.clear();
                    $state.go('questions');
                });
        };


        $scope.delete = function (id) {
            Answer.get({id: $scope.question.id, answerId: id}).$promise.then(function (result) {
                $scope.deleteAnswer = result;
            });
            $('#deleteAnswerConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            Answer.delete({id: $stateParams.id, answerId: id},
                function () {
                    $scope.load($stateParams.id);
                    $('#deleteAnswerConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.imageName = null;
            $scope.deleteAnswer = {id: null, answerText: null, datePosted: null, downvotes: null, upvotes: null};
            $scope.updateAnswer = {id: null, answerText: null, datePosted: null, downvotes: null, upvotes: null};
            $scope.vote = {};
            $scope.deleteQuestion={};
        };

    });
