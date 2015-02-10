'use strict';

angular.module('studentshelpcenterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('questionVote', {
                parent: 'entity',
                url: '/questionVote',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/questionVote/questionVotes.html',
                        controller: 'QuestionVoteController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('questionVote');
                        return $translate.refresh();
                    }]
                }
            })
            .state('questionVoteDetail', {
                parent: 'entity',
                url: '/questionVote/:id',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/questionVote/questionVote-detail.html',
                        controller: 'QuestionVoteDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('questionVote');
                        return $translate.refresh();
                    }]
                }
            });
    });
