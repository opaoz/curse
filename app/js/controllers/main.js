'use strict';
/**
 * @file main.js
 *
 * @author Vladimir_Levin
 * @date 12.10.2015
 */
(function () {
    angular.module('App').controller('MainController', ['config', '_', 'httpRequest', '$scope', 'requests', 'simpleTmpl', '$timeout',
        function (config, _, httpRequest, $scope, requests, simpleTmpl, $timeout) {
            var vm = this;
            vm.markers = [];
            vm.originalMarkers = [];
            vm.year = [];
            vm.types = {/*'Город': false, 'Деревня': false, 'Хутор': false*/};
            vm.objects = {/*'Церковь': false, 'Музей': false, 'Изба-читальня': false*/};
            vm.nearest = {/*'Река': false, 'Гора': false, 'Пруд': false, 'ПГТ': false*/};
            vm.MAX_YEAR = new Date().getFullYear();

            vm.selected = false;

            $scope.$watch('vm.year', _.debounce(filt, 500));
            $scope.$watch('vm.types', _.debounce(filt, 500), true);
            $scope.$watch('vm.nearest', _.debounce(filt, 500), true);
            $scope.$watch('vm.objects', _.debounce(filt, 500), true);

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

            vm.clickOnMarker = function (event, index) {
                //console.log(arguments);
                vm.selected = vm.markers[index];
            };

            function filt() {
                vm.markers = [];
                !$scope.$$phase && $scope.$apply();
                $timeout(function () {
                    vm.markers = angular.copy(_.filter(vm.originalMarkers, function (value) {
                        return (value.minYear <= vm.year[1] && value.maxYear >= vm.year[0]) &&
                            checkProp(vm.types, value.type) &&
                            checkProp(vm.nearest, value.nearest) &&
                            checkProp(vm.objects, value.objects);
                    }));
                    $scope.$apply();
                });


                function checkProp(object, array) {
                    var has = true, needs = [];
                    _.each(object, function (value, key) {
                        if (value) {
                            needs.push(key);
                        }
                    });

                    if (needs.length) {
                        has = false;
                        _.each(needs, function (val) {
                            if (array.indexOf(val) !== -1) {
                                has = true;
                            }
                        });
                    }

                    return has
                }
            }

            function send() {
                httpRequest.send(requests.getByParams).then(function (response) {
                    console.log(response.data);
                    var types = [], nearest = [], objects = [];

                    vm.originalMarkers = _.compact(_.map(response.data, function (value) {
                        value.lat = value.lat.replace(',', '.');
                        value.long = value.long.replace(',', '.');
                        value.pos = [value.lat, value.long];
                        value.type = _.uniq(value.type.split(','));
                        value.nearest = _.uniq(value.nearest.split(','));
                        value.objects = value.objects ? _.uniq(value.objects.split(',')) : [];
                        value.minYear = +value.minYear;
                        value.maxYear = +value.maxYear;

                        types = _.concat(types, value.type);
                        nearest = _.concat(nearest, value.nearest);
                        objects = _.concat(objects, value.objects);

                        return value;
                    }));

                    _.each(_.uniq(types), function (value) {
                        vm.types[value] = false;
                    });

                    _.each(_.uniq(nearest), function (value) {
                        vm.nearest[value] = false;
                    });

                    _.each(_.uniq(objects), function (value) {
                        vm.objects[value] = false;
                    });

                    filt();
                });
            }

            function getMinYear() {
                httpRequest.send(requests.getMinYear).then(function (response) {
                    vm.minYear = response.data[0].minYear;
                    vm.year = [vm.minYear, vm.MAX_YEAR];
                });
            }


            function formatCoords(coords) {
                return _.map(angular.fromJson(coords), function (value) {
                    return [value.latitude, value.longitude];
                });
            }

        }]);
})();