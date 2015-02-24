'use strict';

angular.module('studentshelpcenterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('add_question', {
                parent: 'site',
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
