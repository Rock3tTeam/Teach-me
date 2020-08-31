'use strict';

var app = (function () {
    const constraints = {
        audio: true,
        video: {
            width: 800, height: 600
        }
    };

    // Acceso a la webcam
    async function init() {
        var video = document.getElementById("video");
        var snap = document.getElementById("snap");
        var canvas = document.getElementById("canvas");
        var errorMsgElement = document.querySelector('span#errorMsg');
        // Dibuja la imagen
        var context = canvas.getContext('2d');
        snap.addEventListener("click", function() {
            context.drawImage(video, 0, 0, 640, 480);
        });
        try {
            const stream = await navigator.mediaDevices.getUserMedia(constraints);
            handleSuccess(stream);
        } catch (e) {
            errorMsgElement.innerHTML = `navigator.getUserMedia error:${e.toString()}`;
        }
    }

    // Correcto!
    function handleSuccess(stream) {
        window.stream = stream;
        video.srcObject = stream;
    }

    function drawImage(){

    }

    return {
        init:init
    };
})();

