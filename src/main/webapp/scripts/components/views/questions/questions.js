'use strict';

angular.module('studentshelpcenterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('questions', {
                parent: 'site',
                url: '/questions?search&solved&tags',
                data: {
                    'roles': []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/components/views/questions/questions.html',
                        controller: 'QuestionsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('question');
                        return $translate.refresh();
                    }]

                }
            })
            .state('questionDetails', {
                parent: 'site',
                url: '/questions/{id:[0-9]+}',
                data: {
                    'roles': []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/components/views/questions/questions-details.html',
                        controller: 'QuestionDetailsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('answer');
                        return $translate.refresh();
                    }]
                }
            })
    });
