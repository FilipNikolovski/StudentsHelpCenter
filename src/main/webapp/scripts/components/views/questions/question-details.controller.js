'use strict';

angular.module('studentshelpcenterApp')
    .filter('to_trusted', ['$sce', function ($sce) {
        return function (text) {
            return $sce.trustAsHtml(text);
        };
    }])
    .controller('QuestionDetailsController', function ($scope, $stateParams, Question, Answer, QuestionImage, Account, QuestionVote, AnswerVote, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
        });

        $scope.question = {};
        $scope.vote = {};
        $scope.question.answers = [];
        $scope.question.images = [];

        $scope.deleteAnswer = 0;
        $scope.updateAnswer = {};

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

        $scope.upvoteAnswer = function (answerId) {
            $scope.vote.answer = answerId;
            $scope.vote.vote = 1;
            AnswerVote.save({id: $stateParams.id}, $scope.vote, function () {
                $scope.load($stateParams.id);
                $scope.clear();
            });
        };

        $scope.downvoteAnswer = function (answerId) {
            $scope.vote.answer = answerId;
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

        //TODO Update i delete na answer (momentalno ne raboti)

        $scope.update = function (id) {
            $scope.updateAnswer = Answer.get({id: $scope.question.id, answerId: id});
        };

        $scope.deleteAnswer = function (id) {
            $scope.deleteAnswer = id;
            $('#deleteAnswerConfirmation').modal('show');
        };

        $scope.confirmDelete = function () {
            Answer.delete({id: $stateParams.id, answerId: $scope.deleteAnswer},
                function () {
                    $scope.load($stateParams.id);
                    $('#deleteAnswerConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.answer = {answerText: null, datePosted: null, id: null};
            $scope.imageName = null;
            $scope.deleteAnswer = -1;
            $scope.updateAnswer = {};
            $scope.vote = {};
        };

    });
