
var login = (function () {

    //Esta se cambia por la del heroku antes de desplegar 'http://localhost:8080'
    const urlAPI = 'http://localhost:8080';

    function doLogin() {
        var email = $("#username").val();
        var passw = $("#password").val();
        var loginRequest = {username: email, password: passw};
        var promise = apiclient.postLogin(loginRequest).then(function (data) {
            window.location.replace("http://stackoverflow.com");
            console.log("post succesful");
        });
    }
    return {
        doLogin:doLogin
    };
})();