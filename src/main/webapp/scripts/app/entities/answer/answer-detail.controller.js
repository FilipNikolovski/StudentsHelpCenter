'use strict';

angular.module('studentshelpcenterApp')
    .controller('AnswerDetailController', function ($scope, $stateParams, Answer) {
        $scope.answer = {};
        $scope.load = function (id) {
            Answer.get({id: id}, function(result) {
              $scope.answer = result;
            });
        };
        $scope.load($stateParams.id);
    });
