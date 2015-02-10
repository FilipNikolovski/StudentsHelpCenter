'use strict';

angular.module('studentshelpcenterApp')
    .controller('QuestionImageDetailController', function ($scope, $stateParams, QuestionImage, Question) {
        $scope.questionImage = {};
        $scope.load = function (id) {
            QuestionImage.get({id: id}, function(result) {
              $scope.questionImage = result;
            });
        };
        $scope.load($stateParams.id);
    });
