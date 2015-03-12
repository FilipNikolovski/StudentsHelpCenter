'use strict';

angular.module('studentshelpcenterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('myquestions', {
                parent: 'site',
                url: '/MyQuestions',
                data: {
                    'roles': []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/components/views/questions/questions.html',
                        controller: 'MyQuestionsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('question');
                        return $translate.refresh();
                    }]

                }
            })
    });
