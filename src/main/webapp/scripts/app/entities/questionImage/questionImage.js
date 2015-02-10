'use strict';

angular.module('studentshelpcenterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('questionImage', {
                parent: 'entity',
                url: '/questionImage',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/questionImage/questionImages.html',
                        controller: 'QuestionImageController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('questionImage');
                        return $translate.refresh();
                    }]
                }
            })
            .state('questionImageDetail', {
                parent: 'entity',
                url: '/questionImage/:id',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/questionImage/questionImage-detail.html',
                        controller: 'QuestionImageDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('questionImage');
                        return $translate.refresh();
                    }]
                }
            });
    });
