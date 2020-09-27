
var Modulesearch = (function () {

/**
    function hola(){
        //alert("hola "+localStorage.getItem("name"));
        $("#pid").text(localStorage.getItem("name"))
    }
 */

    function getClasses(){
        apiclient.getClassByName(localStorage.getItem("name"),_table);
    }



    function _map(list){
        return mapList = list.map(function(clase){
            return {
                nombre:clase.nombre,
                description:clase.description,
                fecha_inicio:parseDate(clase.dateOfInit),
                fecha_fin:parseDate(clase.dateOfEnd)

            }
        })
    }

    function parseDate(fecha){
        var datasplit=fecha.split("T");
        var datastring=datasplit.join(" ").split(".")[0].slice(0,-3);
        return datastring
    }

    function _table(classes){
        functions = _map(classes);
        $("#table_class > tbody").empty();
        functions.map(function(c){
            console.log(c);
            $("#table_class > tbody").append(
                "<tr>" +
                "<td>" +"<a href='#'>"+ c.nombre+"</a>" + "</td>"+
                "<td>" + c.description + "</td>"+
                "<td>" + c.fecha_inicio + "</td>"+
                "<td>" + c.fecha_fin + "</td>"+

                "</tr>"
            );
        });
    }

    return {
        getClasses:getClasses
    };
})();


