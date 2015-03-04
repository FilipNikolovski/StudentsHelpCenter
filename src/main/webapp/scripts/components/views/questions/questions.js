'use strict';

angular.module('studentshelpcenterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('questions', {
                parent: 'site',
                url: '/questions?search&solved',
                data: {
                    'roles': []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/components/views/questions/questions.html',
                        controller: 'QuestionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('question');
                        return $translate.refresh();
                    }]

                },
                options:{
                    location: 'fros'
                }
            })
            .state('questionDetails', {
                parent: 'site',
                url: '/questions/:id',
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
