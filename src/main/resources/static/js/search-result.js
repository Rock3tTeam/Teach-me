
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
                description:clase.description

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

                "</tr>"
            );
        });
    }

    return {
        getClasses:getClasses
    };
})();


