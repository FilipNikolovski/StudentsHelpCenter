'use strict';

angular.module('studentshelpcenterApp')
    .controller('QuestionsController', function ($scope, Question) {
        $scope.questions = [];
        $scope.loadAll = function() {
            Question.query(function(result) {
                $scope.questions = result;
            });
        };
        $scope.loadAll();

        $scope.clear = function () {
            $scope.question = {title: null, description: null, datePosted: null, solved: null, id: null};
        };
    });
