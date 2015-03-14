'use strict';

angular.module('studentshelpcenterApp')
    .controller('MyQuestionsController', function ($scope, $stateParams, Question, Principal, $http) {
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
            $http.get('/api/my-questions', {params: {page: $scope.page.currentPage, size: $scope.page.size}}).
                success(function(data, status, headers, config) {
                    $scope.questions = data.content;
                    $scope.page.totalItems = data.totalElements;
                }).
                error(function(data, status, headers, config) {
                });
        };



        $scope.loadAll();

        $scope.pageChanged = function() {

            $http.get('/api/my-questions', {params: {page: $scope.page.currentPage - 1, size: $scope.page.size}}).
                success(function(data, status, headers, config) {
                    $scope.questions = data.content;
                }).
                error(function(data, status, headers, config) {
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
