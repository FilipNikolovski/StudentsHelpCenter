'use strict';

angular.module('studentshelpcenterApp', ['LocalStorageModule', 'tmh.dynamicLocale',
    'ngResource', 'ui.bootstrap', 'ui.router', 'ngCookies', 'pascalprecht.translate', 'ngCacheBuster', 'blueimp.fileupload',
    'textAngular', 'ngTagsInput', 'angular-loading-bar'])

    .run(function ($rootScope, $location, $http, $state, $translate, Auth, Principal, Language, ENV, VERSION) {
        $rootScope.ENV = ENV;
        $rootScope.VERSION = VERSION;
        $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
            $rootScope.toState = toState;
            $rootScope.toStateParams = toStateParams;

            if (Principal.isIdentityResolved()) {
                Auth.authorize();
            }

            // Update the language
            Language.getCurrent().then(function (language) {
                $translate.use(language);
            });
        });

        $rootScope.$on('$stateChangeSuccess',  function(event, toState, toParams, fromState, fromParams) {
            $rootScope.previousStateName = fromState.name;
            $rootScope.previousStateParams = fromParams;
        });

        $rootScope.back = function() {
            // If previous state is 'activate' or do not exist go to 'home'
            if ($rootScope.previousStateName === 'activate' || $state.get($rootScope.previousStateName) === null) {
                $state.go('home');
            } else {
                $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
            }
        };
    })

    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, $translateProvider, tmhDynamicLocaleProvider, httpRequestInterceptorCacheBusterProvider) {

        //enable CSRF
        $httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
        $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';

        //Cache everything except rest api requests
        httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/], true);

        $urlRouterProvider.otherwise('/');
        $stateProvider.state('site', {
            'abstract': true,
            views: {
                'navbar@': {
                    templateUrl: 'scripts/components/navbar/navbar.html',
                    controller: 'NavbarController'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                    $translatePartialLoader.addPart('language');
                    return $translate.refresh();
                }]
            }
        });


        // Initialize angular-translate
        $translateProvider.useLoader('$translatePartialLoader', {
            urlTemplate: 'i18n/{lang}/{part}.json'
        });

        $translateProvider.preferredLanguage('en');
        $translateProvider.useCookieStorage();

        tmhDynamicLocaleProvider.localeLocationPattern('bower_components/angular-i18n/angular-locale_{{locale}}.js');
        tmhDynamicLocaleProvider.useCookieStorage('NG_TRANSLATE_LANG_KEY');


    }).directive("imageResize", [
        "$parse", function($parse) {
            return {
                link: function(scope, elm, attrs) {
                    var imagePercent;
                    imagePercent = $parse(attrs.imagePercent)(scope);
                    elm.bind("load", function(e) {
                        elm.unbind("load"); //Hack to ensure load is called only once
                        var canvas, ctx, neededHeight, neededWidth;
                        neededHeight = elm[0].naturalHeight * imagePercent / 100;
                        neededWidth = elm[0].naturalWidth * imagePercent / 100;
                        canvas = document.createElement("canvas");
                        canvas.width = neededWidth;
                        canvas.height = neededHeight;
                        ctx = canvas.getContext("2d");
                        ctx.drawImage(elm[0], 0, 0, neededWidth, neededHeight);
                        elm.attr('src', canvas.toDataURL("image/jpeg"));
                    });
                }
            };
        }
    ]); ;


