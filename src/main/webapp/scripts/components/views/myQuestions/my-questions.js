'use strict';

angular.module('studentshelpcenterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('myquestions', {
                parent: 'site',
                url: '/my-questions',
                data: {
                    'roles': ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/components/views/questions/my-questions.html',
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
