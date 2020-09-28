
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

    function formatDate(fecha){
        var datasplit=fecha.split("T");
        var datastring=datasplit.join(" ").split(".")[0].slice(0,-3);
        return datastring;
    }

    function _map(list){
        return mapList = list.map(function(clase){
            return {
                nombre:clase.nombre,
                description:clase.description,
                fechaInicio:formatDate(clase.dateOfInit),
                fechaFin:formatDate(clase.dateOfEnd),
                capacity:clase.capacity

            }
        })
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
                "<td>" + c.fechaInicio + "</td>"+
                "<td>" + c.fechaFin + "</td>"+
                "<td>" + c.capacity + "</td>"+

                "</tr>"
            );
        });
    }

    return {
        getClasses:getClasses
    };
})();


