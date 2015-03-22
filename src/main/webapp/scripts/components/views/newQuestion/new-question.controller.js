'use strict';

angular.module('studentshelpcenterApp')
    .controller('AddNewController', function ($scope, $location, $state, $http, Auth, Question, Tag) {
        $scope.question = {};

        var myDropzone = null;

        $scope.dropzoneConfig = {
            'options': {
                'url': '/api/questions/images',
                'autoProcessQueue': false,
                'addRemoveLinks': true,
                'maxFiles': 5,
                'maxFilesize': 5,
                'init': function() {
                    myDropzone = this;
                }
            },
            'eventHandlers': {
                'sending': function (file, xhr, formData) {
                },
                'success': function (file, response) {
                },
                'complete': function (file, response) {
                    $location.path('questions/' + $scope.question.id);
                }
            }
        };

        //Save question
        $scope.create = function () {
            $scope.question.setUser = -1;
            $scope.question.solved = false;
            Question.save($scope.question,
                function (question) {
                    $scope.question = question;
                    myDropzone.on('sending', function(file, xhr, formData) {
                        formData.append('id', question.id);
                        formData.append('_csrf', $.cookie('CSRF-TOKEN'));
                    });
                    myDropzone.processQueue();
                });
        };

        //Load tags for autocomplete
        $scope.loadTags = function (query) {
            return Tag.query({search: query}).$promise;
        };
    });
