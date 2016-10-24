'use strict';
/**
 * @file number.js
 *
 * @author opa_oz
 * @date 24.10.2016
 */
angular.module('App')
    .filter('myNumber', function () {
        return function (value) {
            return Math.round(value);
        };
    });
