/**
 * Created by Владимир on 03.04.2016.
 */
angular.module('App').factory('csv', function () {
    return {
        JSONToCSVConvertor: JSONToCSVConvertor
    };

    function JSONToCSVConvertor(arr, ReportTitle, ShowLabel) {
        //If JSONData is not an object then JSON.parse will parse the JSON string in an Object

        var CSV = [];
        /*        //Set Report title in first row or line

         //This condition will generate the Label/Header
         if (ShowLabel) {
         var row = "";

         //This loop will extract the label from 1st index of on array
         for (var index in arrData[0]) {

         //Now convert each value to string and comma-seprated
         row += index + ';';
         }

         row = row.slice(0, -1);

         //append Label row with line break
         CSV += row + '\r\n';
         }

         //1st loop is to extract each row
         for (var i = 0; i < arrData.length; i++) {
         var row = "";

         //2nd loop will extract each column and convert it in string comma-seprated
         for (var index in arrData[i]) {
         row += '"' + arrData[i][index] + '";';
         }

         row.slice(0, row.length - 1);

         //add a line break after each row
         CSV += row + '\r\n';
         }

         if (CSV == '') {
         alert("Invalid data");
         return;
         }*/

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

        var uri = 'data:text/csv;charset=utf-8,' + encodeURI(CSV.join('\r\n'));
        var link = document.createElement("a");
        link.href = uri;

        link.style = "visibility:hidden";
        link.download = fileName + ".csv";

        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }
});
