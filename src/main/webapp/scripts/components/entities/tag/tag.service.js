'use strict';

angular.module('studentshelpcenterApp')
    .factory('Tag', function ($resource) {
        return $resource('api/tags/:id?search=:search', {search: '@search'}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });
