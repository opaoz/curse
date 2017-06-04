'use strict';
/**
 * @file main.js
 *
 * @author Vladimir_Levin
 * @date 12.10.2015
 */
(function () {
    angular.module('App').controller('MainController', ['$uibModal', 'config', '_', 'httpRequest', '$scope', 'requests', 'simpleTmpl', '$timeout', 'csv', '$location', '$anchorScroll',
        function ($uibModal, config, _, httpRequest, $scope, requests, simpleTmpl, $timeout, csv, $location, $anchorScroll) {
            var vm = this;
            vm.markers = [];
            vm.originalMarkers = [];
            vm.year = [];
            vm.types = {/*'Город': false, 'Деревня': false, 'Хутор': false*/};
            vm.objects = {/*'Церковь': false, 'Музей': false, 'Изба-читальня': false*/};
            vm.nearest = {/*'Река': false, 'Гора': false, 'Пруд': false, 'ПГТ': false*/};
            vm.entranceTo = {};
            vm.MAX_YEAR = new Date().getFullYear();
            vm.searchString = '';
            vm.selectedSettlements = {};
            vm.manualStart = 0;
            vm.groups = {};
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
            $scope.$watch('vm.entranceTo', _.debounce(filt, 500), true);
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

            vm.scrollTo = function (id) {
                $location.hash(id);
                $anchorScroll();
            };

            vm.transform = function (object) {
                object.objects = _
                    .chain(object.objects)
                    .filter(function (v) {
                        return v.year >= vm.year[0] && v.year <= vm.year[1];
                    })
                    .map('name')
                    .value();

                return object;
            };

            vm.ok = function () {
                vm.selectedItems = _.chain(vm.markers).filter(function (v) {
                    return vm.selectedSettlements[v.max.name];
                }).map('max').value();

                setTimeout(function () {
                    $('#wordExportGlobal').wordExport('settlements-export');
                }, 500);
            };

            vm.downloadWordAll = function () {
                var modalInstance = $uibModal.open({
                    ariaLabelledBy: 'modal-title',
                    ariaDescribedBy: 'modal-body',
                    templateUrl: 'myModalContent.html',
                    controllerAs: 'vm',
                    controller: function ($uibModalInstance) {
                        this.markers = vm.markers;
                        this.ok = function () {
                            vm.ok();
                            $uibModalInstance.close();
                        };
                        this.selectedSettlements = vm.selectedSettlements;
                    }
                });

                modalInstance.result.then(function () {

                }, function () {
                    console.info('Modal dismissed at: ' + new Date());
                });
            };

            vm.clickOnMarker = function (event, index) {
                vm.selected = angular.copy(vm.markers[index]);
                vm.selected.max = vm.transform(vm.selected.max);
                vm.selected.array = _.uniqBy(vm.selected.array, 'name');
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
                _.each(vm.groups, function (v, k) {
                    vm.groups[k] = false;
                });

                _.each(vm.demo, function (value) {
                    value.show = (value.minYear <= vm.currentYear && value.maxYear >= vm.currentYear);

                    if (value.isSettlement) {
                        value.show = false;
                    }

                    if (value.show) {
                        vm.groups[value.group] = true;
                    }
                });

                _.each(vm.groups, function (v, k) {
                    if (!v) {
                        _.each(vm.demo, function (value) {
                            if (value.group === k && value.isSettlement) {
                                value.show = true;
                                vm.groups[k] = true;
                            }
                        });
                    }
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
                    markers = _.filter(angular.copy(vm.originalMarkers), function (value) {
                        var searchStr = true;
                        if (vm.searchString) {
                            searchStr = value.name.toLowerCase().replace('ё', 'е').indexOf(vm.searchString.toLowerCase().replace('ё', 'е')) !== -1
                        }

                        value.maxRange = value.maxYear - vm.year[1];
                        if (value.maxRange < 0) {
                            value.maxRange = 10000000;
                        }

                        value.objects = _.filter(value.objects, function (v) {
                            return v.year >= vm.year[0] && v.year <= vm.year[1];
                        });

                        return (value.minYear <= vm.year[1] && value.maxYear >= vm.year[0]) &&
                            checkProp(vm.types, value.type) &&
                            checkProp(vm.nearest, value.nearest) &&
                            checkProp(vm.entranceTo, value.entranceTo) &&
                            checkProp(vm.objects, value.objects) && searchStr;
                    });

                    fillFilters(markers);

                    markers = _.map(_.groupBy(markers, 'group'), function (value) {
                        return {array: value, max: _.minBy(value, 'maxRange')};
                    });

                    if (!_.isEqual(old, markers) || _.isEmpty(old)) {
                        vm.markers = [];
                        !$scope.$$phase && $scope.$apply();
                        vm.markers = markers;
                    }

                    $scope.$apply();
                });


                function checkProp(object, array) {
                    if (!_.isEmpty(array) && _.first(array).name) {
                        array = _.map(array, 'name');
                    }

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
                    var i = 0;
                    var geocoder = new google.maps.Geocoder();

                    var originalMarkers = _.compact(mapping(response.data));

                    var groups = _.groupBy(originalMarkers, 'group');
                    var count = _.keys(groups).length;
                    var result = {};

                    _.each(groups, function (value) {
                        var max = _.maxBy(value, 'maxYear');

                        if(_.isEmpty(max) || _.isEmpty(max.group)){
                        return;
                        }

                        geocoder.geocode({
                            'address': 'Саратовская область, ' + max.group
                        }, function (results, status) {
                            i++;
                            if (status === google.maps.GeocoderStatus.OK) {
                                var Lat = results[0].geometry.location.lat();
                                var Lng = results[0].geometry.location.lng();

                                result[max.group] = {lat: Lat, long: Lng, pos: [Lat, Lng]};
                            }

                            if (i === count) {
                                vm.originalMarkers = _.map(originalMarkers, function (v) {
                                    v.lat = result[v.group].lat || v.lat;
                                    v.long = result[v.group].long || v.long;
                                    v.pos = result[v.group].pos || v.pos;

                                    return v;
                                });
                                filt();
                            }
                        });

                    });
                });
            }

            function fillFilters(array) {
                var types = [], nearest = [], objects = [], entranceTo = [];

                _.each(array, function (value) {
                    types = _.concat(types, value.type);
                    nearest = _.concat(nearest, value.nearest);
                    objects = _.concat(objects, _.map(value.objects, 'name'));
                    entranceTo = _.concat(entranceTo, value.entranceTo);
                });

                vm.types = compact(vm.types);
                vm.nearest = compact(vm.nearest);
                vm.objects = compact(vm.objects);
                vm.entranceTo = compact(vm.entranceTo);

                _.each(_.uniq(types), function (value) {
                    vm.types[value] = !!vm.types[value];
                });

                _.each(_.uniq(nearest), function (value) {
                    vm.nearest[value] = !!vm.nearest[value];
                });

                _.each(_.uniq(objects), function (value) {
                    vm.objects[value] = !!vm.objects[value];
                });

                _.each(_.uniq(entranceTo), function (value) {
                    vm.entranceTo[value] = !!vm.entranceTo[value];
                });

                vm.types = sort(vm.types);
                vm.nearest = sort(vm.nearest);
                vm.objects = sort(vm.objects);
                vm.entranceTo = sort(vm.entranceTo);

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
                    vm.minYear = +response.data[0].year.replace(config.prefixes[0], '');
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

            function mapping(array) {
                var prefixes = config.prefixes;
                var settlements = {};

                array = _.map(array, function (obj) {
                    var value = [];

                    _.each(obj, function (v, k) {
                        _.each(prefixes, function (prefix) {
                            if (!_.isEmpty(v)) {
                                v = v.replace(prefix, '');
                            }
                        });
                        if (_.isEmpty(v)) {
                            v = undefined;
                        }
                        value[k] = v;
                    });

                    return value;
                });

                _.each(array, function (value) {
                    if (!value.settlementHasBeginning) {
                        return;
                    }

                    var result = {};

                    result.lat = '51.32';
                    result.long = '46';
                    result.pos = [value.lat, value.long];

                    result.type = [value.settlementTypeSetlement || 'Село'];
                    result.nearest = [];
                    result.entranceTo = [(value.settlementEntranceTo || '').replace('_', ' ')];
                    result.minYear = +value.settlementHasBeginning;
                    result.maxYear = vm.MAX_YEAR;
                    result.name = value.settlementName;
                    result.group = value.settlement;
                    result.population = value.settlementPopulation;
                    result.geo = value.settlementHasGeographicalArrangement ? value.settlementHasGeographicalArrangement.replace('_', ' ') : '';
                    result.desc = 'Not yet';
                    result.founders = value.settlementFounders;
                    result.isSettlement = true;

                    if (!_.isEmpty(value.dbpediaDesc)) {
                        result.dbpediaDesc = value.dbpediaDesc;
                    }

                    if (!_.isEmpty(value.dbpediaUpdate)) {
                        result.dbpediaUpdate = new Date(parseInt(value.dbpediaUpdate, 10));
                    }

                    if (value.settlementSource && value.settlementSource.indexOf('вики') !== -1) {
                        result.sourceUrl = value.settlementSourceUrl;
                    }


                    if (!_.isEmpty(value.culturalObjects)) {
                        result.objectsYears = (value.culturalObjectsYears || '').split(',');
                        result.objectsYears = _.map(result.objectsYears, function (v) {
                            _.each(config.prefixes, function (prefix) {
                                v = v.replace(prefix, '');
                            });

                            return v;
                        });

                        result.objects = _.uniq(value.culturalObjects.split(','));
                        result.objects = _.map(result.objects, function (v, k) {
                            return {name: v, year: parseInt(result.objectsYears[k], 10)};
                        });
                    }


                    var objects = result.objects;
                    result.objects = undefined;

                    if ((result.name.indexOf('Арка') !== -1 && objects[0].name.indexOf('музей') !== -1)
                        || (result.name.indexOf('Баска') !== -1 && objects[0].name.indexOf('ВОВ') !== -1)) {
                        result.objects = objects;
                    }

                    if (!_.isEmpty(settlements[result.group])) {
                        settlements[result.group] = _.defaults(settlements[result.group], result);
                    } else {
                        settlements[result.group] = result;
                    }
                    vm.groups[result.group] = false;
                });

                array = _.map(array, function (value) {
                    var result = {};

                    result.lat = '51.32';
                    result.long = '46';
                    result.pos = [value.lat, value.long];

                    result.type = value.typeSetlement ? [value.typeSetlement] : undefined;
                    result.nearest = [];
                    result.entranceTo = value.entranceTo ? [value.entranceTo.replace('_', ' ')] : undefined;
                    result.minYear = +value.hasBeginning;
                    result.maxYear = +value.hasEnd;
                    result.name = value.name;
                    result.group = value.settlement;
                    result.population = value.population;
                    result.desc = 'No desc';
                    result.range = result.maxYear - result.minYear;
                    result.isSettlement = false;
                    result.sourceUrl = value.sourceUrl;
                    result.sourceDesc = value.sourceDesc;

                    return _.defaults(result, settlements[result.group]);
                });

                settlements = _.map(settlements, function (v) {
                    return v;
                });

                return _.concat(array, settlements);
            }
        }]);
})();