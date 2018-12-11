function save_user_info_change(){
    var data = {'u_sex': $('#show_u_sex').text(),
    'u_nickname': $('#u_nickname').text()
    };

    $.ajax({
        type: 'POST',
        url: '/change_user_info',
        data: JSON.stringify(data),  //转化字符串
        contentType: 'application/json; charset=UTF-8',
        success: function (data_return) { //成功的话，得到消息
            var code_return = data_return["code"];
            if(code_return === 200){
                alert("修改成功");
            }
            else if(code_return === 400){
                alert("UNKNOWN ERROR");
            }
            window.location.href = "index.html";

        }
    });
}

function get_user_info(){
    var data = get_query_object(window.location.href);
    $.ajax({
        type: 'POST',
        url: '/user',
        data: JSON.stringify(data),  //转化字符串
        contentType: 'application/json; charset=UTF-8',
        success: function (data_return) { //成功的话，得到消息
            var user_data = eval(data_return);
            //$('#u_photo').text(user_data[0]["u_photo"]);

            $('#show_u_sex').text(user_data[0]["u_sex"]);
            //$('#u_sex').attr('placeholder', user_data[0]["u_sex"]);
            $('#uni_name').attr('placeholder', user_data[0]["uni_name"]);
            $('#u_nickname').text(user_data[0]["u_nickname"]);
            $('#user-avatar').attr('src', user_data[0]["u_photo"]);

            if (user_data[0].hasOwnProperty('u_email')) {
                $('#privacyInfo').css('display', "block");
                $('#u_email').attr('placeholder', user_data[0]["u_email"]);
                $('#u_create_time').attr('placeholder', user_data[0]["u_create_time"]);
                $('#u_permissions').attr('placeholder', user_data[0]["u_permissions"]);
                $('#send_message_button').attr("data-nickname", "").css('display', "none");
            }
            else{
                $('#u_sex').find('a').attr('onclick', '');
                $('#privacyInfo').css('display', "none");
                $('#send_message_button').attr("data-nickname", user_data[0]["u_nickname"]).css('display', "block");
            }
            //window.location.href = "user.html";
        }
    });
}