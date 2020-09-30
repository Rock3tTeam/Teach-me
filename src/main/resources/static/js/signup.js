var ModuleSignup = (function () {

    function validate(email,firstName,lastName,password){
        var bool = true;

        if (firstName=="") {
            bool = false;
            console.log("nombre")
        }

        else if (lastName==""){
            bool = false;
            console.log("ape")
        }

        else if (password==""){
            bool = false;
            console.log("pass")
        }

        else if (!(/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(email))){
            bool = false;
            console.log("email")
        }

        return bool;

    }



    function createUser(){

        var email = document.getElementById("email").value;
        var firstName = document.getElementById("name_signup").value;
        var lastName = document.getElementById("last_name").value;
        var password = document.getElementById("pass").value;
        var description = document.getElementById("description_signup").value;
        var bool=validate(email,firstName,lastName,password);

        if(bool){
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
        }





    };


    return {
        createUser,createUser

    };

})();