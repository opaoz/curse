/**
 * Created by Владимир on 03.04.2016.
 */
angular.module('App').factory('csv', function () {
    return {
        JSONToCSVConvertor: JSONToCSVConvertor
    };

    function JSONToCSVConvertor(arr, ReportTitle, ShowLabel) {
        var CSV = [];

        CSV.push(['Наименование', 'Описание', 'Тип', 'Близлежащие объекты', 'Годы существования', 'Координаты', 'Сопутствующие названия'].join(';'));

        _.each(arr, function (value) {
            var names = [];
            console.log(value);

            CSV.push([
                value.max.name.toString().replace(/\n/g, ''),
                value.max.desc.toString().replace(/\n/g, ''),
                value.max.type.join(', ').replace(/\n/g, ''),
                value.max.nearest.join(', ').replace(/\n/g, ''),
                value.max.minYear + ' - ' + value.max.maxYear,
                value.max.pos.join(', ')
            ].join(';'));
        });

        var fileName = "";
        fileName += ReportTitle.replace(/ /g, "_");

        var uri = 'data:text/csv;charset=utf-8,\ufeff' + encodeURI(CSV.join('\r\n'));
        var link = document.createElement("a");
        link.href = uri;

        link.style = "visibility:hidden";
        link.download = fileName + ".csv";

        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }
});
