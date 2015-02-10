'use strict';

angular.module('studentshelpcenterApp')
    .controller('TagController', function ($scope, Tag) {
        $scope.tags = [];
        $scope.loadAll = function() {
            Tag.query(function(result) {
               $scope.tags = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Tag.save($scope.tag,
                function () {
                    $scope.loadAll();
                    $('#saveTagModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.tag = Tag.get({id: id});
            $('#saveTagModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.tag = Tag.get({id: id});
            $('#deleteTagConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            Tag.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTagConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.tag = {name: null, id: null};
        };
    });
