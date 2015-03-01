'use strict';

angular.module('studentshelpcenterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('questions', {
                parent: 'site',
                url: '/questions?searchString&solved',
                data: {
                    roles: ['ROLE_USER']
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
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/components/views/questions/questionsDetails.html',
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
