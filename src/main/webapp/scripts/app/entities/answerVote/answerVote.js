'use strict';

angular.module('studentshelpcenterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('answerVote', {
                parent: 'entity',
                url: '/answerVote',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/answerVote/answerVotes.html',
                        controller: 'AnswerVoteController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('answerVote');
                        return $translate.refresh();
                    }]
                }
            })
            .state('answerVoteDetail', {
                parent: 'entity',
                url: '/answerVote/:id',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/answerVote/answerVote-detail.html',
                        controller: 'AnswerVoteDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('answerVote');
                        return $translate.refresh();
                    }]
                }
            });
    });
