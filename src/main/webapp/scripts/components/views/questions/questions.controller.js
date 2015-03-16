'use strict';

angular.module('studentshelpcenterApp')
    .controller('QuestionsController', function ($scope, $stateParams, Question, Principal, QuestionVote) {
        Principal.identity().then(function (account) {
            $scope.account = account;
        });

        $scope.questions = [];
        $scope.page = {
            totalItems: 0,
            currentPage: 0,
            size: 5
        };
        $scope.vote=null;

        $scope.deleteQuestion = {};
        $scope.loadAll = function () {
            Question.query({search: $stateParams.search, solved: $stateParams.solved, page: $scope.page.currentPage, size: $scope.page.size}).$promise
                .then(function(questions) {
                    $scope.questions = questions.content;
                    $scope.page.totalItems = questions.totalElements;

                    Principal.identity().then(function (account) {
                    angular.forEach($scope.questions, function(question) {
                            question.userVotedPositive=false;
                            question.userVotedNegative=false;
                           QuestionVote.get({id: question.id, userId: account.id}).$promise.then(function(vote){
                               console.log(vote);
                               if(vote != null && vote.vote == 1)
                               {
                                   question.userVotedPositive=true;
                                   question.userVotedNegative=false;
                               }
                               else if (vote != null && vote.vote == -1){
                                   question.userVotedPositive=false;
                                   question.userVotedNegative=true;
                               }
                               else
                               {
                                   question.userVotedPositive=false;
                                   question.userVotedNegative=false;
                               }
                           });
                        });
                    });
                });


        };

        $scope.loadAll();

        $scope.pageChanged = function() {
            Question.query({page: $scope.page.currentPage - 1, size: $scope.page.size}).$promise
                .then(function (questions) {
                    $scope.questions= questions.content;
                });
        };

        $scope.questionDelete = function (question) {
            $scope.deleteQuestion = question;
            $('#deleteQuestionConfirmation').modal('show');
        };

        $scope.confirmQuestionDelete = function () {
            Question.delete({id: $scope.deleteQuestion.id},
                function () {
                    $('#deleteQuestionConfirmation').modal('hide');
                    $scope.clear();
                    $scope.loadAll();
                });
        };

        $scope.clear = function () {
            $scope.question = {title: null, description: null, datePosted: null, solved: null, id: null};
            $scope.deleteQuestion = null;
        };
    });
