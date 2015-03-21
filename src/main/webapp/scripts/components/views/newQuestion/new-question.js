'use strict';

angular.module('studentshelpcenterApp')
    .config(['$stateProvider', function ($stateProvider) {
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
    }]);
