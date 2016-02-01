'use strict';
/**
 * @file main.js
 *
 * @author Vladimir_Levin
 * @date 12.10.2015
 */
(function () {
    angular.module('App').controller('MainController', ['config','httpRequest', function (config, httpRequest) {
        var vm = this;

        vm._init_ = function () {
            vm.query = {
                    title: 'Parent',
                    text: 'SELECT ?child ?parent WHERE {?child rela:childOf ?parent.}'
                };
            vm.output = 'Text will be here';
        };

        vm.send = function (data) {
            httpRequest.send('PREFIX rela: <http://purl.org/vocab/relationship/>' + data.text).then(function (response) {
                console.log(response);
                vm.output = response.data;
            });
        };
    }]);
})();