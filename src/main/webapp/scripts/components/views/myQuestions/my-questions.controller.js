'use strict';

angular.module('studentshelpcenterApp')
    .controller('MyQuestionsController', function ($scope, $stateParams, Question, Tag, Principal, $http) {
        $scope.questions = [];
        $scope.page = {
            totalItems: 0,
            currentPage: 0,
            size: 5
        };

        $scope.queue = [];
        $scope.editQuestion=null;

        Principal.identity().then(function (account) {
            $scope.account = account;
        });

        $scope.questionEdit=function(question) {
            $scope.editQuestion=question;
        };

        $scope.loadTags = function(query) {
            return Tag.query().$promise;
        };

        $scope.create = function () {
            $scope.editQuestion.setUser = -1;
            $scope.editQuestion.solved = false;
            Question.save($scope.editQuestion, function(question) {
                $scope.editQuestion=null;
            });

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
            $scope.deleteQuestion = null;
        };

        $scope.goBack=function() {
            $scope.editQuestion=null;
        };
    })
    .controller('FileDestroyController', [
        '$scope', '$http',
        function ($scope, $http) {
            var file = $scope.file,
                state;
            if (file.url) {
                file.$state = function () {
                    return state;
                };
                file.$destroy = function () {
                    state = 'pending';
                    return $http({
                        url: file.deleteUrl,
                        method: file.deleteType
                    }).then(
                        function () {
                            state = 'resolved';
                            $scope.clear(file);
                        },
                        function () {
                            state = 'rejected';
                        }
                    );
                };
            } else if (!file.$cancel && !file._index) {
                file.$cancel = function () {
                    $scope.clear(file);
                };
            }
        }
    ]);
