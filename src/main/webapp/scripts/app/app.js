'use strict';

angular.module('coreApp', ['LocalStorageModule', 'tmh.dynamicLocale', 'pascalprecht.translate', 
               'ui.bootstrap', // for modal dialogs
    'ngResource', 'ui.router', 'ngCookies', 'ngCacheBuster', 'ngFileUpload', 'infinite-scroll'])
    .run(function ($rootScope, $location, $window, $http, $state, $translate, Language, Auth, Principal, Facebook, ENV, VERSION) {
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
            var titleKey = 'global.title' ;

            $rootScope.previousStateName = fromState.name;
            $rootScope.previousStateParams = fromParams;

            // Set the page title key to the one configured in state or use default one
            if (toState.data.pageTitle) {
                titleKey = toState.data.pageTitle;
            }
            
            $translate(titleKey).then(function (title) {
                // Change window title with translated one
                $window.document.title = title;
            });
            
        });

        $rootScope.back = function() {
            // If previous state is 'activate' or do not exist go to 'home'
            if ($rootScope.previousStateName === 'activate' || $state.get($rootScope.previousStateName) === null) {
                $state.go('home');
            } else {
                $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
            }
        };

        $window.fbAsyncInit = function() {
            // Executed when the SDK is loaded

            FB.init({ 

              /* 
               The app id of the web app;
               To register a new app visit Facebook App Dashboard
               ( https://developers.facebook.com/apps/ ) 
              */

              appId: '404823203038294', 

              /* 
               Adding a Channel File improves the performance 
               of the javascript SDK, by addressing issues 
               with cross-domain communication in certain browsers. 
              */

              //channelUrl: 'app/channel.html', 

              /* 
               Set if you want to check the authentication status
               at the start up of the app 
              */

              status: true, 

              /* 
               Enable cookies to allow the server to access 
               the session 
              */

              cookie: true, 

              /* Parse XFBML */

              xfbml: true,
              /*
               use version 2.3 
              */
              version: 'v2.3' 
            });
            Facebook.watchLoginChange();
        };
        // Are you familiar to IIFE ( http://bit.ly/iifewdb ) ?

          (function(d){
            // load the Facebook javascript SDK

            var js, 
            id = 'facebook-jssdk', 
            ref = d.getElementsByTagName('script')[0];

            if (d.getElementById(id)) {
              return;
            }

            js = d.createElement('script'); 
            js.id = id; 
            js.async = true;
            js.src = "//connect.facebook.net/en_US/all.js";

            ref.parentNode.insertBefore(js, ref);

          }(document));
    })
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, $translateProvider, tmhDynamicLocaleProvider, httpRequestInterceptorCacheBusterProvider) {

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
                }]
            }
        });

        $httpProvider.interceptors.push('errorHandlerInterceptor');
        $httpProvider.interceptors.push('authExpiredInterceptor');
        $httpProvider.interceptors.push('authInterceptor');
        $httpProvider.interceptors.push('notificationInterceptor');
        
        // Initialize angular-translate
        $translateProvider.useLoader('$translatePartialLoader', {
            urlTemplate: 'i18n/{lang}/{part}.json'
        });

        $translateProvider.preferredLanguage('en');
        $translateProvider.useCookieStorage();
        $translateProvider.useSanitizeValueStrategy('escaped');
        $translateProvider.addInterpolation('$translateMessageFormatInterpolation');

        tmhDynamicLocaleProvider.localeLocationPattern('bower_components/angular-i18n/angular-locale_{{locale}}.js');
        tmhDynamicLocaleProvider.useCookieStorage();
        tmhDynamicLocaleProvider.storageKey('NG_TRANSLATE_LANG_KEY');
    })
    .directive("fbLoginButton", function() {
    return {
        restrict: 'E',
        link: function (scope, iElement, iAttrs) {
            if (FB) {
                FB.XFBML.parse(iElement[0].parent);
            }
        }
      }
    });
