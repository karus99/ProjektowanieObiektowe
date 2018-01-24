// my crappy webpage
// Karol Drwila
// copyright 2018

$(document).ready(function()
{
    $.get("http://ipinfo.io", function(response)
    {
        $.ajax({
            url: '/api/log/entry',
            data:
                {
                    ip: response.ip
                },
            type: 'POST',
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
    }, "jsonp");
});

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

$('#put1_submit').click(function()
{
    $.ajax({
        url: '/api/element',
        data:
            {
                key: $('#put1_key').val(),
                name: $('#put1_name').val(),
                qty: $('#put1_qty').val()
            },
        type: 'PUT',
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