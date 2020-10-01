var ModuleSignup = (function () {

    function validate(email,firstName,lastName,password){
        var bool = true;

        if (firstName=="") {
            bool = false;
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'The name cannot be empty'
            })
        }

        else if (lastName==""){
            bool = false;
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'The last name cannot be empty'
            })
        }

        else if (password==""){
            bool = false;
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'The password cannot be empty'
            })
        }

        else if (!(/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(email))){
            bool = false;
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Enter a valid email'
            })
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

                const Toast = Swal.mixin({
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 3000,
                    width: 300,
                    timerProgressBar: true,
                    didOpen: (toast) => {
                        toast.addEventListener('mouseenter', Swal.stopTimer)
                        toast.addEventListener('mouseleave', Swal.resumeTimer)
                    }
                })

                Toast.fire({
                    icon: 'success',
                    title: 'Signed in successfully'
                })

            }).catch(e => {
                console.log("error post")
            })
        }


    };


    return {
        createUser,createUser

    };

})();

