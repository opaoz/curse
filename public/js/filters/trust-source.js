'use strict';

angular.module('App')
    .filter('trust', function ($sce) {
        return function (value, source) {
            var result;
            switch (source.toLowerCase()) {
                case 'url':
                    result = $sce.trustAsResourceUrl(value);
                    break;
                case 'html':
                    result = $sce.trustAsHtml(value);
                    break;
                case 'js':
                    $sce.trustAsJs(value);
                    break;
                default:
                    result = value;
            }
            return result;
        };
    });
