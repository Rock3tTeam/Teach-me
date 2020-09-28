

$("#datetimepicker_create").datetimepicker({
    language: 'en',
    format: 'YYYY-MM-DD HH:mm:ss',

});

$("#datetimepicker_create").datetimepicker();


$("#datetimepicker_create1").datetimepicker({
    language: 'en',
    format: 'YYYY-MM-DD HH:mm:ss',

});

$("#datetimepicker_create1").datetimepicker();


var ModuleCreate = (function () {

    function formatDate(element){
        var data = document.getElementById(element).value;
        var datasplit=data.split(" ");
        var datastring=datasplit.join("T");
        console.log(datastring);
        return datastring;

    }

    
    function createClass(){

        var datastring=formatDate("datetimepickercreate_input");
        console.log(datastring);
        var datastring1=formatDate("datetimepickercreate_input1");
        console.log(datastring1);
        var name = document.getElementById("class_name").value;
        var description = document.getElementById("description_class").value;
        var capacity = document.getElementById("class_capacity").value;
        var clase = {
            "nombre":name,
            "capacity":capacity,
            "description":description,
            "amountOfStudents": 20,
            "dateOfInit": datastring,
            "dateOfEnd": datastring1
            };
        apiclient.postClass("nicolas@gmail.com",clase).then(function (){
            console.log("post succesful");
            });
        };




    return {
        createClass: createClass

    };
})();
