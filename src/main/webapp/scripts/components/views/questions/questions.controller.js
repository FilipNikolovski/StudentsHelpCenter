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

        $scope.vote = null;

        $scope.loadAll = function () {
            Question.query({
                search: $stateParams.search,
                solved: $stateParams.solved,
                tags: $stateParams.tags,
                page: $scope.page.currentPage,
                size: $scope.page.size
            })
                .$promise.then(function (questions) {
                    $scope.questions = questions.content;
                    $scope.page.totalItems = questions.totalElements;
                });
        };

        $scope.loadAll();

        $scope.pageChanged = function () {
            Question.query({
                search: $stateParams.search, solved: $stateParams.solved, tags: $stateParams.tags,
                page: $scope.page.currentPage - 1, size: $scope.page.size
            })
                .$promise.then(function (questions) {
                    $scope.questions = questions.content;
                });
        };

        $scope.clear = function () {
            $scope.question = {title: null, description: null, datePosted: null, solved: null, id: null};
            $scope.deleteQuestion = null;
        };
    });
