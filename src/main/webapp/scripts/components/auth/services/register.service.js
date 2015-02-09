'use strict';

angular.module('studentshelpcenterApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


