'use strict';

angular.module('studentshelpcenterApp')
    .filter('to_trusted', ['$sce', function($sce){
        return function(text) {
            return $sce.trustAsHtml(text);
        };
    }])
    .controller('QuestionDetailsController', function ($scope, $stateParams, Question, Answer, QuestionImage, Account, QuestionVote, AnswerVote, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
        })
        $scope.positive=1;
        $scope.negative=-1;
        $scope.question = {};
        $scope.vote={};
        $scope.question.answers = [];
        $scope.question.images = [];
        $scope.deleteAnswer=0;
        $scope.updateAnswer={};
        $scope.load = function (id) {
            Question.get({id: id}, function(result) {
                $scope.question = result;
            });

            Answer.query({id: id}, function (answers) {
                $scope.question.answers = answers;
            });

            angular.forEach($scope.answers, function (answer) {
                AnswerVote.query({id: answer.id}, function (votes) {
                    answer.votes=votes;
                });
            });

            QuestionImage.query({id: id}, function (images) {
                $scope.question.images = images;
            });

            QuestionVote.query({id: id}, function (votes) {
                $scope.question.votes = votes;
            });


        };

        $scope.addVote=function(){
            $scope.vote.question=$scope.question;
            $scope.vote.vote=1;
            $scope.vote.user=$scope.account;
            QuestionVote.save({id: $stateParams.id}, $scope.vote, function(){
                $scope.load($stateParams.id);
                $scope.clear();
            });
        };

        $scope.addNVote=function(){
            $scope.vote.question=$scope.question;
            $scope.vote.vote=-1;
            $scope.vote.user=$scope.account;
            QuestionVote.save({id: $stateParams.id}, $scope.vote, function(){
                $scope.load($stateParams.id);
                $scope.clear();
            });
        };

        $scope.addAnswerVote=function(answer){
            $scope.vote.answer=answer;
            $scope.vote.vote=1;
            $scope.vote.user=$scope.account;
            AnswerVote.save({id: $stateParams.id, answerId:answer.id}, $scope.vote, function(){
                $scope.load($stateParams.id);
                $scope.clear();
            });
        };

        $scope.addAnswerNVote=function(answer){
            $scope.vote.answer=answer;
            $scope.vote.vote=-1;
            $scope.vote.user=$scope.account;
            AnswerVote.save({id: $stateParams.id, answerId:answer.id}, $scope.vote, function(){
                $scope.load($stateParams.id);
                $scope.clear();
            });
        };

        $scope.showImage=function(imageName){
            $scope.imageName=imageName;
            console.log(imageName);
            $('#showImage').modal('show');

        };

        $scope.load($stateParams.id);

        $scope.create = function () {
            $scope.updateAnswer.datePosted = new Date();
            $scope.updateAnswer.question=$scope.question;
            $scope.updateAnswer.user=$scope.account;
            Answer.save({id: $stateParams.id}, $scope.updateAnswer,
                function () {
                    $scope.load($stateParams.id);
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.updateAnswer = Answer.get({id: $scope.question.id, answerId: id});


        };

        $scope.delete = function (id) {
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
            $scope.imageName=null;
            $scope.deleteAnswer=0;
            $scope.updateAnswer=null;
            $scope.vote=null;
        };
    });
