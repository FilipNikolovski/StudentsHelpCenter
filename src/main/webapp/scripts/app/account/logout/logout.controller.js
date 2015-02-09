'use strict';

angular.module('studentshelpcenterApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
