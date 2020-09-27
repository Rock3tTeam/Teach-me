var Moduleindex = (function () {


    function  setName(){
        var name = $("#class_search").val();
        localStorage.setItem("name",name);
    }


    return {
        setName:setName

    };
})();