'use strict';

angular.module('studentshelpcenterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('addQuestion', {
                parent: 'site',
                url: '/add_question',
                views: {
                    'content@': {
                        templateUrl: 'scripts/components/views/new_question/new_question.html',
                        controller: 'AddNewController'
                    }
                },
                resolve: {

                }
            });
    });
