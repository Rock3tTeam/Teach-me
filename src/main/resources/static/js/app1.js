

$('#datetimepicker').datetimepicker({
    language: 'en',
    format: 'YYYY-MM-DD hh:mm',

});

$('#datetimepicker1').datetimepicker({
    language: 'en',
    format: 'YYYY-MM-DD hh:mm',

});

$('#datetimepicker2').datetimepicker({
    language: 'en',
    format: 'YYYY-MM-DD hh:mm',

});


var d = new Date();



var datestring = d.getFullYear()  + "-" + (d.getMonth()+1) + "-" + d.getDay() + " " +
    d.getHours() + ":" + d.getMinutes();

$('#iddatetimepicker').val(datestring);

$('#iddatetimepicker1').val(datestring);

$('#datetimepicker1').datetimepicker();

