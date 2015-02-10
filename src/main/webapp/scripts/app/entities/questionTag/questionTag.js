'use strict';

angular.module('studentshelpcenterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('questionTag', {
                parent: 'entity',
                url: '/questionTag',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/questionTag/questionTags.html',
                        controller: 'QuestionTagController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('questionTag');
                        return $translate.refresh();
                    }]
                }
            })
            .state('questionTagDetail', {
                parent: 'entity',
                url: '/questionTag/:id',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/questionTag/questionTag-detail.html',
                        controller: 'QuestionTagDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('questionTag');
                        return $translate.refresh();
                    }]
                }
            });
    });
