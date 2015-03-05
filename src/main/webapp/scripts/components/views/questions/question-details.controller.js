'use strict';

angular.module('studentshelpcenterApp')
    .filter('to_trusted', ['$sce', function($sce){
        return function(text) {
            return $sce.trustAsHtml(text);
        };
    }])
    .controller('QuestionDetailsController', function ($scope, $stateParams, Question, Answer, QuestionImage, Account) {

        $scope.question = {};
        $scope.vote={};
        $scope.vote.user={};
        $scope.vote.user.id=0;
        $scope.load = function (id) {
            Question.get({id: id}, function(result) {
                $scope.question = result;
            });

            Answer.query({id: id}).$promise
                .then(function (answers) {
                    $scope.question.answers = answers;
                });

            QuestionImage.query({id: id}).$promise
                .then(function (images) {
                    $scope.question.images = images;
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
            $scope.imageName=null;
        };
    });
