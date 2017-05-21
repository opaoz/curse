'use strict';
/**
 * @file app.js
 *
 * @author Vladimir_Levin
 * @date 12.10.2015
 */
(function () {
    angular.module('App', [
        'ngMap',
        'ngAnimate',
        'ui.bootstrap',
        'ui.bootstrap-slider',
        'pageslide-directive',
        'ui.checkbox',
        'angular-animated-modal',
        'counter',
        'ngRoute'
    ])
        .config(function ($routeProvider, $locationProvider) {
            $routeProvider
                .when('/', {
                    templateUrl: 'js/views/about.html'
                })
                .when('/tool', {
                    templateUrl: 'js/views/tool.html',
                    controller: 'MainController',
                    controllerAs: 'vm'
                }).otherwise({redirectTo: '/'});
        })
        .constant('requests', (function () {
            var request = {};

            request.getByParams = 'get';
            request.getMinYear = 'year';

            return request;
        })())
        .constant('config', {
            queryUrl: 'http://127.0.0.1:8080',
            prefixes: [
                "http://www.semanticweb.org/анна/ontologies/2016/4/untitled-ontology-41#",
                "^^http://www.w3.org/2001/XMLSchema#int",
                "^^http://www.w3.org/2000/01/rdf-schema#Literal",
                "null"
            ]
        });
})();