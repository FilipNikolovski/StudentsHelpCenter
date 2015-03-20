'use strict';

angular.module('studentshelpcenterApp')
    .controller('MyQuestionsController', function ($scope, $filter, $stateParams, Question, Tag, Principal, $http, QuestionVote, QuestionImage) {
        $scope.questions = [];
        $scope.page = {
            totalItems: 0,
            currentPage: 0,
            size: 5
        };

        $scope.queue = [];
        $scope.editQuestion = null;

        // initial image index
        $scope._Index = 0;

        Principal.identity().then(function (account) {
            $scope.account = account;
        });

        // if a current image is the same as requested image
        $scope.isActive = function (index) {
            return $scope._Index === index;
        };

        // show prev image
        $scope.showPrev = function () {
            $scope._Index = ($scope._Index > 0) ? --$scope._Index : $scope.photos.length - 1;
        };

        // show next image
        $scope.showNext = function () {
            $scope._Index = ($scope._Index < $scope.photos.length - 1) ? ++$scope._Index : 0;
        };

        // show a certain image
        $scope.showPhoto = function (index) {
            $scope._Index = index;
        };

        $scope.ofIndex=function (arr, obj){
            for(var i = 0; i < arr.length; i++){
                if(angular.equals(arr[i].imageName, obj)){
                    return i;
                }
            };
            return -1;
        }


        $scope.showImage = function (imageName) {
            $scope.imageName = imageName;
            var index=$scope.ofIndex($scope.photos, imageName);
            $scope.showPhoto(index);
            $('#showImage').modal('show');
        };


        $scope.questionEdit = function (question) {
            $scope.editQuestion = question;
            QuestionImage.query({id: question.id}).$promise.then(function (images) {
                $scope.editQuestion.images = images;
                $scope.photos = $scope.editQuestion.images;
            });
        };

        $scope.loadTags = function (query) {
            return Tag.query().$promise;
        };

        $scope.create = function () {
            $scope.editQuestion.setUser = -1;
            $scope.editQuestion.solved = false;
            Question.save($scope.editQuestion, function (question) {
                $scope.editQuestion = null;
            });

        };

        $scope.deleteQuestion = {};
        $scope.loadAll = function () {
            $http.get('/api/my-questions', {params: {page: $scope.page.currentPage, size: $scope.page.size}}).
                success(function (data, status, headers, config) {
                    $scope.questions = data.content;
                    $scope.page.totalItems = data.totalElements;

                    Principal.identity().then(function (account) {
                        angular.forEach($scope.questions, function (question) {
                            question.userVotedPositive = false;
                            question.userVotedNegative = false;
                            QuestionVote.get({id: question.id, userId: account.id}).$promise.then(function (vote) {
                                console.log(vote);
                                if (vote != null && vote.vote == 1) {
                                    question.userVotedPositive = true;
                                    question.userVotedNegative = false;
                                }
                                else if (vote != null && vote.vote == -1) {
                                    question.userVotedPositive = false;
                                    question.userVotedNegative = true;
                                }
                                else {
                                    question.userVotedPositive = false;
                                    question.userVotedNegative = false;
                                }
                            });
                        });
                    });
                }).
                error(function (data, status, headers, config) {
                });
        };

        $scope.loadAll();

        $scope.pageChanged = function () {

            $http.get('/api/my-questions', {params: {page: $scope.page.currentPage - 1, size: $scope.page.size}}).
                success(function (data, status, headers, config) {
                    $scope.questions = data.content;
                }).
                error(function (data, status, headers, config) {
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

        $scope.goBack = function () {
            $scope.editQuestion = null;
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
