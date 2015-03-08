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
        });
        $scope.question = {};
        $scope.vote={};
        $scope.question.answers = [];
        $scope.question.images = [];
        $scope.vote.user={};
        $scope.vote.user.id=0;
        $scope.user={};
        $scope.deleteAnswer={};
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
            Account.get().$promise
                .then(function (account) {
                    $scope.vote.user.id=account.data.id;
                    console.log($scope.vote.user.id);
                })
            //$scope.vote.question.id=id;
            //$scope.vote.vote=vote;
            console.log($scope.vote.user.id);

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
            Answer.save({id: $stateParams.id}, $scope.updateAnswer,
                function () {
                    $scope.load($stateParams.id);
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.updateAnswer = Answer.get({id: id});

        };

        $scope.delete = function (id) {
            $scope.deleteAnswer = Answer.get({id: $scope.question.id, answerId: id});
            $('#deleteAnswerConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            Answer.delete({}, {id: $scope.question.id, answerId: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAnswerConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.answer = {answerText: null, datePosted: null, id: null};
            $scope.imageName=null;
            $scope.deleteAnswer=null;
            $scope.updateAnswer=null;
        };
    });
