

$('#datetimepicker_select').datetimepicker({
    language: 'en',
    format: 'YYYY-MM-DD HH:mm:ss',

});

$('#datetimepicker_select1').datetimepicker({
    language: 'en',
    format: 'YYYY-MM-DD HH:mm:ss',

});


$('#datetimepicker_teach').datetimepicker({
    language: 'en',
    format: 'YYYY-MM-DD HH:mm:ss',

});

$('#datetimepicker_teach1').datetimepicker({
    language: 'en',
    format: 'YYYY-MM-DD HH:mm:ss',

});


/**
var button = document.getElementById('create_button');

console.log(button);

button.addEventListener('click', function () {

    var data = document.getElementById('datetimepickercreate_input').value;
    console.log(data);
});
 */


var d = new Date();


var datestring = d.getFullYear()  + "-" + ("0"+(d.getMonth()+1)).slice(-2) + "-" + ("0"+d.getDate()).slice(-2) + " " +
    ("0"+d.getHours()).slice(-2)  + ":" + ("0"+d.getMinutes()).slice(-2) +":"+("0"+d.getSeconds()).slice(-2) ;


$('#datetimepickerselect_input').val(datestring);
$('#datetimepickerselect_input1').val(datestring);

$('#datetimepickerteach_input').val(datestring);
$('#datetimepickerteach_input1').val(datestring);

