'use strict';
/**
 * @file main.js
 *
 * @author Vladimir_Levin
 * @date 12.10.2015
 */
(function () {
    angular.module('App').controller('MainController', ['config', '_', 'httpRequest', '$scope', 'requests', 'simpleTmpl', '$timeout', 'csv',
        function (config, _, httpRequest, $scope, requests, simpleTmpl, $timeout, csv) {
            var vm = this;
            vm.markers = [];
            vm.originalMarkers = [];
            vm.year = [];
            vm.types = {/*'Город': false, 'Деревня': false, 'Хутор': false*/};
            vm.objects = {/*'Церковь': false, 'Музей': false, 'Изба-читальня': false*/};
            vm.nearest = {/*'Река': false, 'Гора': false, 'Пруд': false, 'ПГТ': false*/};
            vm.MAX_YEAR = new Date().getFullYear();
            vm.searchString = '';
            vm.manualStart = 0;
            vm.options = {
                animatedIn: 'zoomIn',
                animatedOut: 'zoomOut',
                animationDuration: '.6s',
                overflow: 'hidden'
            };

            vm.selected = false;

            $scope.$watch('vm.year', _.debounce(filt, 500));
            $scope.$watch('vm.searchString', _.debounce(filt, 500));
            $scope.$watch('vm.types', _.debounce(filt, 500), true);
            $scope.$watch('vm.nearest', _.debounce(filt, 500), true);
            $scope.$watch('vm.objects', _.debounce(filt, 500), true);

            $scope.$watch('vm.exactYear', _.debounce(exact, 500));

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
                vm.selected = vm.markers[index];
                vm.pageslide = true;
            };

            vm.word = function (name) {
                $('#word-export').wordExport(name);
            };

            vm.downloadCSV = function () {
                csv.JSONToCSVConvertor(vm.markers, 'Report', true);
            };

            vm.afterClose = function () {
                vm.currentYear = vm.minYear;
                vm.manualStart = vm.minYear;
                vm.demonstration = false;
                vm.demo = [];
            };

            vm.beforeOpen = function () {
                console.log('beforeOpen');
                vm.demonstration = true;
                vm.manualStart = vm.MAX_YEAR;
                vm.demo = angular.copy(vm.originalMarkers);

                $scope.$apply();
            };

            vm.step = function () {
                _.each(vm.demo, function (value) {
                    value.show = (value.minYear <= vm.currentYear && value.maxYear >= vm.currentYear);
                });

                $scope.$applyAsync();
            };

            function exact() {
                var year = parseInt(vm.exactYear, 10);

                if (year >= vm.minYear && year <= vm.MAX_YEAR) {
                    vm.year = [year, year];
                    !$scope.$$phase && $scope.$apply();
                }
            }

            function filt() {
                var old = vm.markers;

                var markers = [];

                $timeout(function () {
                    markers = angular.copy(_.filter(vm.originalMarkers, function (value) {
                        var searchStr = true;
                        if (vm.searchString) {
                            searchStr = value.name.toLowerCase().replace('ё', 'е').indexOf(vm.searchString.toLowerCase().replace('ё', 'е')) !== -1
                        }

                        return (value.minYear <= vm.year[1] && value.maxYear >= vm.year[0]) &&
                            checkProp(vm.types, value.type) &&
                            checkProp(vm.nearest, value.nearest) &&
                            checkProp(vm.objects, value.objects) && searchStr;
                    }));

                    fillFilters(markers);

                    markers = _.map(_.groupBy(markers, 'group'), function (value) {
                        return {array: value, max: _.maxBy(value, 'maxYear')};
                    });

                    if (!_.isEqual(old, markers) || _.isEmpty(old)) {
                        vm.markers = [];
                        !$scope.$$phase && $scope.$apply();
                        vm.markers = markers;
                    }

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

                    vm.originalMarkers = _.compact(_.map(response.data, function (value) {
                        value.lat = value.lat.replace(',', '.');
                        value.long = value.long.replace(',', '.');
                        value.pos = [value.lat, value.long];
                        value.type = _.uniq(value.type.split(','));
                        value.nearest = _.uniq(value.nearest.split(','));
                        value.objects = value.objects ? _.uniq(value.objects.split(',')) : [];
                        value.minYear = +value.minYear;
                        value.maxYear = +value.maxYear;

                        return value;
                    }));

                    filt();
                });
            }

            function fillFilters(array) {
                var types = [], nearest = [], objects = [];

                _.each(array, function (value) {
                    types = _.concat(types, value.type);
                    nearest = _.concat(nearest, value.nearest);
                    objects = _.concat(objects, value.objects);
                });

                vm.types = compact(vm.types);
                vm.nearest = compact(vm.nearest);
                vm.objects = compact(vm.objects);
                _.each(_.uniq(types), function (value) {
                    vm.types[value] = !!vm.types[value];
                });

                _.each(_.uniq(nearest), function (value) {
                    vm.nearest[value] = !!vm.nearest[value];
                });

                _.each(_.uniq(objects), function (value) {
                    vm.objects[value] = !!vm.objects[value];
                });

                vm.types = sort(vm.types);
                vm.nearest = sort(vm.nearest);
                vm.objects = sort(vm.objects);

                return array;
            }

            function compact(object) {
                var result = {};
                _.each(object, function (v, k) {
                    if (v) {
                        result[k] = v;
                    }
                });

                return result;
            }

            function sort(object) {
                var result = {}, fields = [];

                _.each(object, function (v, k) {
                    fields.push(k);
                });

                fields = _.sortBy(fields);
                _.each(fields, function (v) {
                    result[v] = object[v];
                });

                return result;
            }

            function getMinYear() {
                httpRequest.send(requests.getMinYear).then(function (response) {
                    vm.minYear = response.data[0].minYear;
                    vm.currentYear = vm.minYear;
                    vm.manualStart = vm.minYear;
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