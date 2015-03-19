'use strict';

angular.module('studentshelpcenterApp')
    .controller('AddNewController', function ($scope, $location, $state, $http, Auth, Question, Tag) {
        $scope.question = {};

        $scope.queue = [];

        //Save question
        $scope.create = function () {
            $scope.question.setUser = -1;
            $scope.question.solved = false;

            Question.save($scope.question,
                function(question) {
                    var fd = new FormData();
                    angular.forEach($scope.queue, function(value, key) {
                        fd.append('files', value);
                    });

                    $http.post('/api/questions/'+question.id+'/upload-images', fd, {
                        headers: {'Content-Type': 'multipart/form-data; boundary=CreateQuestion' }
                    })
                        .success(function() {
                            $location.path('/questions/' + question.id);
                        })
                        .error();


                });

        };

        //Load tags for autocomplete
        $scope.loadTags = function(query) {
            return Tag.query().$promise;
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
