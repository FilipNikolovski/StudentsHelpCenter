'use strict';

angular.module('studentshelpcenterApp')
    .config(['$stateProvider', 'fileUploadProvider', function ($stateProvider, fileUploadProvider) {
        $stateProvider
            .state('addQuestion', {
                parent: 'site',
                url: '/add_question',
                data: {
                    'roles': ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/components/views/newQuestion/new-question.html',
                        controller: 'AddNewController'
                    }
                }
            });

        angular.extend(fileUploadProvider.defaults, {
            // Enable image resizing, except for Android and Opera,
            // which actually support image resizing, but fail to
            // send Blob objects via XHR requests:
            disableImageResize: /Android(?!.*Chrome)|Opera/
                .test(window.navigator.userAgent),
            maxFileSize: 5000000,
            acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i
        });
    }]);
