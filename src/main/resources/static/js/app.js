"use strict";

var app = (function () {
    const constraints = {
        audio: true,
        video: {
            width: 700, height: 500
        }
    };

    // Correcto!
    function handleSuccess(stream) {
        window.stream = stream;
        video.srcObject = stream;
    }

    // Acceso a la webcam
    async function init() {
        var video = document.getElementById("video");
        var errorMsgElement = document.querySelector("span#errorMsg");

        try {
            const stream = await navigator.mediaDevices.getUserMedia(constraints);
            handleSuccess(stream);
        } catch (e) {
            errorMsgElement.innerHTML = `navigator.getUserMedia error:${e.toString()}`;
        }
    }

    return {
        init:init
    };
})();

