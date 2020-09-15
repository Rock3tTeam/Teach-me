

$('#datetimepicker_create').datetimepicker({
    language: 'en',
    format: 'YYYY-MM-DD HH:mm:ss',

});

$('#datetimepicker_create').datetimepicker();


$('#datetimepicker_create1').datetimepicker({
    language: 'en',
    format: 'YYYY-MM-DD HH:mm:ss',

});

$('#datetimepicker_create1').datetimepicker();


var button = document.getElementById('create_button');

console.log(button);

button.addEventListener('click', function () {
    var data = document.getElementById('datetimepickercreate_input').value;
    var datasplit=data.split(" ");
    var datastring=datasplit.join("T")
    console.log(datastring);
    var data1 = document.getElementById('datetimepickercreate_input1').value;
    var datasplit1=data1.split(" ");
    var datastring1=datasplit1.join("T")
    console.log(datastring1);
});
