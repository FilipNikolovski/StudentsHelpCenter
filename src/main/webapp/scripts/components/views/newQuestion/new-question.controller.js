'use strict';

var url = 'api/questionImages';

angular.module('studentshelpcenterApp')
    .controller('AddNewController', function ($scope, $location, $state, $http, Auth, Question) {
        $scope.question = {};

        $scope.queue = [];

        $scope.options = {
            url: url
        };

        //Save question
        $scope.create = function () {

        };

    });
