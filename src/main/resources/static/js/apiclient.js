apiclient = (function () {

    function postClass(userEmail,clase){
        var data = $.ajax({
            url: "http://localhost:8080/api/v1/users/"+userEmail+"/classes",
            type: 'POST',
            data: JSON.stringify(clase),
            contentType: "application/json"
        });
        return data;
    }

    return {
        postClass:postClass
    };

})();