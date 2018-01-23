// my crappy webpage
// Karol Drwila
// copyright 2018

$('[name="get4_elements"]').change(function()
{
    var value = $(this).val();

    $('#get4_a').attr('href', '/element/' + value);
    $('#get4_badge').html('/element/' + value);
});

$('#delete1_submit').click(function()
{
    $.ajax({
        url: '/api/element',
        data:
            {
                key: $('#delete1_key').val()
            },
        type: 'DELETE',
        success: function(result)
        {
            if(result.status == "OK")
            {
                console.log("works");
            }
            else
            {
                console.error(result);
            }
        }
    });
});