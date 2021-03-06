'use strict';

angular.module('studentshelpcenterApp')
    .controller('MyQuestionsController', function ($scope, $filter, $stateParams, Question, Tag, Principal, $http, QuestionVote, QuestionImage) {
        var myDropzone = null;

        $scope.questions = [];
        $scope.deleteQuestion = {};
        $scope.editQuestion = null;
        $scope._Index = 0; //initial image index
        $scope.page = {
            totalItems: 0,
            currentPage: 0,
            size: 5
        };

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
                    $scope.editQuestion = null;
                }
            }
        };

        $scope.loadAll = function () {
            $http.get('/api/my-questions', {params: {page: $scope.page.currentPage, size: $scope.page.size}}).
                success(function (data, status, headers, config) {
                    $scope.questions = data.content;
                    $scope.page.totalItems = data.totalElements;
                }).
                error(function (data, status, headers, config) {
                });
        };

        $scope.pageChanged = function () {
            $http.get('/api/my-questions', {params: {page: $scope.page.currentPage - 1, size: $scope.page.size}}).
                success(function (data, status, headers, config) {
                    $scope.questions = data.content;
                }).
                error(function (data, status, headers, config) {
                });
        };

        $scope.loadAll();//Load all questions

        Principal.identity().then(function (account) {
            $scope.account = account;
        });

        // if a current image is the same as requested image
        $scope.isActive = function (index) {
            return $scope._Index === index;
        };

        // show prev image
        $scope.showPrev = function () {
            $scope._Index = ($scope._Index > 0) ? --$scope._Index : $scope.editQuestion.images.length - 1;
        };

        // show next image
        $scope.showNext = function () {
            $scope._Index = ($scope._Index < $scope.editQuestion.images.length - 1) ? ++$scope._Index : 0;
        };

        // show a certain image
        $scope.showPhoto = function (index) {
            $scope._Index = index;
        };

        $scope.ofIndex = function (arr, obj) {
            for (var i = 0; i < arr.length; i++) {
                if (angular.equals(arr[i], obj)) {
                    return i;
                }
            }
            return -1;
        };

        $scope.showImage = function (image) {
            $scope.image = image;
            var index = $scope.ofIndex($scope.editQuestion.images, image);
            $scope.showPhoto(index);
            $('#showImage').modal('show');
        };

        $scope.deleteImage = function () {
            $scope.image = $scope.editQuestion.images[$scope._Index];
            $('#deleteImageConfirmation').modal('show');
        };

        $scope.confirmImageDelete = function () {
            QuestionImage.delete({id: $scope.editQuestion.id, imageId: $scope.image.id},
                function () {
                    $scope.clear();
                    QuestionImage.query({id: $scope.editQuestion.id}).$promise.then(function (images) {
                        $scope.editQuestion.images = images;
                    });
                    $('#deleteImageConfirmation').modal('hide');
                });
        };

        $scope.create = function () {
            $scope.editQuestion.setUser = -1;
            $scope.editQuestion.solved = false;
            Question.save($scope.editQuestion, function (question) {
                myDropzone.on('sending', function(file, xhr, formData) {
                    formData.append('id', question.id);
                    formData.append('_csrf', $.cookie('CSRF-TOKEN'));
                });
                myDropzone.processQueue();
            });
        };

        $scope.questionEdit = function (question) {
            $scope.editQuestion = question;
            QuestionImage.query({id: question.id}).$promise.then(function (images) {
                $scope.editQuestion.images = images;
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

        $scope.loadTags = function (query) {
            return Tag.query().$promise;
        };

        $scope.clear = function () {
            $scope.deleteQuestion = null;
        };

        $scope.goBack = function () {
            $scope.editQuestion = null;
        };
    });
