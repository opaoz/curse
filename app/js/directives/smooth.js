'use strict';
/**
 * @file smooth.js
 *
 * @author opa_oz
 * @date 11.12.2016
 */
angular.module('App').directive('smooth', function () {
    return {
        link: function (scope, elem, attrs) {
            elem.on('click', function (event) {
                event.preventDefault();
                $('html, body').animate({
                    scrollTop: $(attrs.smooth).offset().top
                }, 500);
            });
        }
    }
});
