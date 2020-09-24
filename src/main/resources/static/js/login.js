
//const axios = require('axios').default;

var login = (function () {

    //Esta se cambia por la del heroku antes de desplegar 'http://localhost:8080'
    const url = 'https://teach2-me.herokuapp.com/'

    function loginPost() {
        var email = $("#your_email").val();
        var passw = $("#your_pass").val();
        var loginRequest = JSON.stringify({ name: email, password: passw });
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
         const promise = new Promise((resolve,reject) => {
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
         });
    }

    return {
        loginPost:loginPost
    };
})();