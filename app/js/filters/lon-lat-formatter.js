'use strict';
/**
 * @file lon-lat-formatter.js
 *
 * @author Vladimir_Levin
 * @date 09.11.2015
 */
(function () {
    angular.module('App').filter('LonLat', function () {
        return function (obj) {
            if (!obj) {
                return obj;
            }
            return obj.latitude + ', ' + obj.longitude;
        }
    });
})();