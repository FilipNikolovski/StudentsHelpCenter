'use strict';

angular.module('studentshelpcenterApp')
    .controller('QuestionController', function ($scope, $stateParams, Question) {
        $scope.questions = [];

        $scope.page = {
            totalItems: 0,
            currentPage: 0,
            size: 5
        };

        $scope.loadAll = function () {
            Question.query({page: $scope.page.currentPage, size: $scope.page.size})
                .$promise.then(function (questions) {
                    $scope.questions = questions.content;
                    $scope.page.totalItems = questions.totalElements;
                });
        };

        $scope.pageChanged = function () {
            Question.query({page: $scope.page.currentPage - 1, size: $scope.page.size})
                .$promise.then(function (questions) {
                    $scope.questions = questions.content;
                });
        };

        $scope.loadAll();

        $scope.create = function () {
            $scope.question.setUser = 1;
            Question.save($scope.question,
                function () {
                    $scope.loadAll();
                    $('#saveQuestionModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.question = Question.get({id: id});
            $('#saveQuestionModal').modal('show');
        };

        $scope.deleteQuestion = function (id) {
            $scope.question = Question.get({id: id});
            $('#deleteQuestionConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            Question.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteQuestionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.question = {title: null, description: null, datePosted: null, solved: null, id: null};
        };
    });
