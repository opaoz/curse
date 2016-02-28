'use strict';
/**
 * @file main.js
 *
 * @author Vladimir_Levin
 * @date 12.10.2015
 */
(function () {
    angular.module('App').controller('MainController', ['config', '_', 'httpRequest', '$scope', 'requests', 'simpleTmpl',
        function (config, _, httpRequest, $scope, requests, simpleTmpl) {
            var vm = this;
            vm.markers = [];
            vm.year = [];
            vm.types = {'City': false, 'Village': false, 'Bowery': false};
            vm.objects = {'Church': false, 'Museum': false, 'Reading home': false};
            vm.nearest = {'River': false, 'Mountain': false, 'Pool': false};
            vm.MAX_YEAR = new Date().getFullYear();

            vm._init_ = function () {
                vm.map = {
                    center: {
                        latitude: 51.262370,
                        longitude: 46.790909
                    },
                    zoom: 7
                };
                send();
                getMinYear();
                vm.paths = formatCoords(config.coordArray);
            };

            function send() {
                httpRequest.send(simpleTmpl.format(requests.getByParams, {
                    year: getFormattedYear(vm.year),
                    types: getFormattedType(vm.types)
                })).then(function (response) {
                    console.log(response.data);
                    vm.markers = _.compact(_.map(response.data, function (value) {
                        value.lat = value.lat.replace(',', '.');
                        value.long = value.long.replace(',', '.');
                        if (!value.years || value.years.length <= 0 || !value.types || value.types.length <= 0) {
                            return null;
                        }
                        return value;
                    }));
                });
            }

            function getMinYear() {
                httpRequest.send(requests.getMinYear).then(function (response) {
                    vm.minYear = response.data[0].minYear;
                    vm.year = [vm.minYear, vm.MAX_YEAR];
                });
            }

            function getFormattedType(value) {
                var result = ['&&'];

                _.each(value, function (value, key) {
                    var str = '?type = "' + key + '"';
                    if (value) {
                        if (result.length > 1) {
                            str = '|| ' + str;
                        }
                        result.push(str);
                    }
                });

                return result.length == 1 ? '' : result.join(' ');
            }

            function getFormattedYear(value) {
                return '?year >= "' + value[0] + '" && ?year <= "' + value[1] + '"';
            }

            $scope.$watch('vm.year', _.debounce(send, 500));
            $scope.$watch('vm.types', _.debounce(send, 500), true);

            function formatCoords(coords) {
                return _.map(angular.fromJson(coords), function (value) {
                    return [value.latitude, value.longitude];
                });
            }

        }]);
})();