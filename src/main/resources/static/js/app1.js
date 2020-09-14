

$('#datetimepicker').datetimepicker({
    language: 'en',
    format: 'YYYY-MM-dd hh:mm',

});

$('#datetimepicker1').datetimepicker({
    language: 'en',
    format: 'YYYY-MM-dd hh:mm',

});


var d = new Date();

let weekday = ['Sun', 'Mon', 'Tue', 'Wed', 'Th', 'Fri', 'Sat'][d.getDay()]

var datestring = d.getFullYear()  + "-" + (d.getMonth()+1) + "-" + weekday + " " +
    d.getHours() + ":" + d.getMinutes();

$('#iddatetimepicker').val(datestring);

$('#datetimepicker1').datetimepicker();

