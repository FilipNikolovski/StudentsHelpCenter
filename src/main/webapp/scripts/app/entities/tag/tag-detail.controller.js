'use strict';

angular.module('studentshelpcenterApp')
    .controller('TagDetailController', function ($scope, $stateParams, Tag) {
        $scope.tag = {};
        $scope.load = function (id) {
            Tag.get({id: id}, function(result) {
              $scope.tag = result;
            });
        };
        $scope.load($stateParams.id);
    });
