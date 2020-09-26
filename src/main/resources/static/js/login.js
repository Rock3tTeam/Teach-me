
//const axios = require('axios').default;

var login = (function () {

    //Esta se cambia por la del heroku antes de desplegar 'http://localhost:8080'
    const urlAPI = 'http://localhost:8080';

    function loginPost() {
        var email = $("#username").val();
        var passw = $("#password").val();
        var loginRequest = JSON.stringify({ username: email, password: passw });
        console.log("hola");
        /*const options = {
          headers: {'Content-Type': 'application/json'}
        };
        axios({
          method: 'post',
          url: url+'/api/v1/login',
          data: loginRequest,
          headers: {'Content-Type': 'application/json'}
        });
        axios.post(url+'/api/v1/login', loginRequest, options)
        .then(response => {
            console.log(response);//resultado exitoso
        }).catch(e => {
            console.log(e); // error
        });*/
        var promise = $.post({
            url:urlAPI+'/login',
            data: loginRequest,
            contentType: "application/json",
        });

        promise.then(function(data){
            console.log("logueado");
        },function(data){
            alert("Error, correo electrónico o contraseña inválidos.");
        });
         /*const promise = new Promise((resolve,reject) => {
            $.ajax({
                url: url+'/api/v1/login',
                type: 'POST',
                data: loginRequest,
                contentType: "application/json"
            }).done(function (data) {
                console.log(data);
                resolve('SUCCESS');
            }).fail(function (msg) {
                reject('FAIL');
            });
         });
         promise.then(res =>{
            alert(res);
         })
         .catch(error => {
            alert(error);
         });*/
    }

    return {
        loginPost:loginPost
    };
})();