/**
 * Created by Владимир on 28.02.2016.
 */
angular.module('App').factory('simpleTmpl', ['_', function (_) {
    return {
        format: function (formatString, pattern) {
            return _.template(formatString/*.replace(/\{([\w\d\-_]+)\}/g, '<%= $1 %>')*/)(pattern)
        }
    };
}
]);
