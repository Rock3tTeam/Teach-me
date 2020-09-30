var ModuleSignup = (function () {

    function createUser(){

        var email = document.getElementById("email").value;
        var firstName = document.getElementById("name_signup").value;
        var lastName = document.getElementById("last_name").value;
        var password = document.getElementById("pass").value;
        var description = document.getElementById("description_signup").value;

        var user = {
            "email":email,
            "firstName":firstName,
            "lastName":lastName,
            "password": password,
            "description": description
        };
        console.log(user);
        apiclient.postUser(user).then(function (){
            console.log("post succesful");
        });
    };


    return {
        createUser,createUser

    };

})();