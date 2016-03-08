'use strict';
/**
 * @file http-request.js
 *
 * @author Vladimir_Levin
 * @date 12.10.2015
 */
(function () {
    angular.module('App').service('httpRequest', ['$http', 'config', function ($http, config) {
        this.send = function (message) {
            return $http({
                url: config.queryUrl,
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                data: message
            });
        }
    }]);
})();