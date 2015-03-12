'use strict';

angular.module('studentshelpcenterApp')
    .controller('MyQuestionsController', function ($scope, $stateParams, Question, Principal) {
        Principal.identity().then(function (account) {
            $scope.account = account;
        });

        $scope.questions = [];
        $scope.page = {
            totalItems: 0,
            currentPage: 0,
            size: 5
        };

        $scope.deleteQuestion = {};
        $scope.loadAll = function () {
            Question.query({page: $scope.page.currentPage, size: $scope.page.size}).$promise
                .then(function(questions) {
                    $scope.questions = questions.content;
                    $scope.page.totalItems = questions.totalElements;
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
