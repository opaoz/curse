'use strict';
/**
 * @file code-formatter.js
 *
 * @author Vladimir_Levin
 * @date 04.11.2015
 */
(function () {
    angular.module('App').filter('codeFormatter', function () {
        return function (value) {
            if (angular.isObject(value) || angular.isArray(value)) {
                value = angular.toJson(value);
            }

            if (!angular.isString(value)) {
                return value;
            }

            return value.replace(/(\[|,|\{)/g, '$1\n')
                .replace(/]/g, '\n]')
                .replace(/"([a-z]+)":/g, '\t\t<code style="color:blue;">$1: </code>')
                .replace(/"http:\/\/family\/([a-z]+)"/g, '<code style="color:maroon">"$1"</code>')
                .replace(/\{/g, '\t{')
                .replace(/}/g, '\n\t}');
        };
    });
})();