apiclient = (function () {

    function postClass(userEmail,clase){
        var data = $.ajax({

            url: "https://teach2-me.herokuapp.com/api/v1/users/"+userEmail+"/classes",
            type: 'POST',
            data: JSON.stringify(clase),
            contentType: "application/json"
        });
        return data;
    }

    function getClassByName(class_name, callback) {
        $.getJSON("http://localhost:8080/api/v1/classes?name="+class_name, function (data) {
            callback(data);
        });
    }

    return {
        postClass:postClass,
        getClassByName:getClassByName
    };

})();