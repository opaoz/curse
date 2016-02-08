'use strict';
/**
 * @file main.js
 *
 * @author Vladimir_Levin
 * @date 12.10.2015
 */
(function () {
    angular.module('App').controller('MainController', ['config', '_', 'httpRequest', function (config, _, httpRequest) {
        var vm = this;

        vm._init_ = function () {
            vm.map = {
                center: {
                    latitude: 51.262370,
                    longitude: 46.790909
                },
                zoom: 7
            };

            vm.paths = formatCoords(config.coordArray);
            httpRequest.send('PREFIX r: <http://purl.org/vocab/relationship/>' +
                'SELECT * WHERE {' +
                '   ?city r:latitude ?lat; ' +
                '         r:longitude ?long;' +
                '         r:name ?name.' +
                '   {' +
                '      select (group_concat(?type) as ?types) (group_concat(?year) as ?years) WHERE {' +
                '          ?city r:year ?year;' +
                '                r:type ?type.' +
                '      } GROUP BY ?city' +
                '   }' +
                '}').then(function (response) {
                console.log(response.data);
            });
        };

        function formatCoords(coords) {
            return _.map(angular.fromJson(coords), function (value) {
                return [value.latitude, value.longitude];
            });
        }

    }]);
})();