function sent_message() {
    var detail = $('#insert_information_details').val();
    if(sessionStorage.getItem("u_id")===null){
        alert("请先登录");
        window.location.href = "login.html";
    }
    else if(detail!==''){
        var data = {'details': detail, 'receiver': sessionStorage.getItem("recevier_nickname")};
        $.ajax({
            type: 'POST',
            url: '/message/sending',
            data: JSON.stringify(data),  //转化字符串
            contentType: 'application/json; charset=UTF-8',
            success: function (data_return) {
                var code_return = data_return["code"];
                if(code_return === 200){
                    alert("发送成功");
                    $('#mry-opo,#mry-mask').remove();
                }
                else if(code_return === 400){
                    alert("发送失败");
                }
            }
        });
    }
    else{
        alert("发送内容不可为空");
    }

}

function pop_up_message(thisel){
    sessionStorage.setItem("recevier_nickname", $(thisel).data("nickname"));
    if( $(thisel).data("pm_id")!==null){
        // 不为空，将此私信视为已回复，改变数据库中相应状态
        $(thisel).parents(".reply-item").css('background-color', '');
        $.ajax({
            type: 'POST',
            url: '/message/update_status',
            data: JSON.stringify({'pm_id': $(thisel).data("pm_id")}),
            contentType: 'application/json; charset=UTF-8',
            success: function (data_return) {
                var messages = eval(data_return);
                var len = messages.length;
                if(len===0){
                    $('#show_message').append('<p>这还什么都没有</p>');
                }
                else{
                    for(var i=0;i<len;i++){
                        add_message_list(messages[i]);
                    }
                }

            }
        });
    }
    var astr='<strong>私信内容</strong>' +
        '<textarea class="form-control" rows="10" id="insert_information_details"></textarea>' +
        '<button class="btn btn-success" onclick="sent_message()">发送信息</button>';
    pop({height:350,width:600,title: '输入信息内容', content:astr})
}

function add_message_list(data){
    var child = '<div class="row reply-item">' +
        '                <div class="col-md-1 top-left">' +
        '                    <a class="sender-avatar"> <img src="../image/favicon.ico" style="width: 100%; height: auto;"/>' +
        '                    </a>' +
        '                </div>' +
        '                <div class="col-md-11 top-right">' +
        '                    <div class="name-box"><a target="_blank"\n' +
        '                                             class="name sender-nickname" style="font-size: 14px;margin-right: 6px;"></a>' +
        '                        <span class="message-time" style="font-size: 12px;"></span>' +
        '                    </div>' +
        '                    <div class="comment-content"><span\n' +
        '                            class="text">' +
        '                        </span></div>' +
        '                    <button class="btn reply-btn" onclick="pop_up_message(this)">回复</button>' +
        '                </div>' +
        '            </div><br/>';
    var theroot = $('#show_message').append(child);
    theroot = theroot.find(".reply-item").last();
    theroot.find(".sender-avatar").attr("onclick", "jump_to_user_index('" + data['u_nickname'] + "')");
    theroot.find(".sender-nickname").text(data['u_nickname']).attr("onclick", "jump_to_user_index('" +
        data['u_nickname'] + "')");
    theroot.find(".message-time").text(data['pm_sending_time']);
    theroot.find(".comment-content").text(data['pm_details']);
    theroot.find(".reply-btn").attr("data-nickname", data['u_nickname']);
    theroot.find(".reply-btn").attr("data-pm_id", data['pm_id']);
    if(data['pm_status']===0){
        theroot.css("background-color", "rgba(216,191,216,0.3)")
    }
}

function refresh_message_list() {
    if(sessionStorage.getItem('u_id')===null){
        alert('请先登录');
        window.location.href = "login.html";
        return;
    }
    $.ajax({
        type: 'GET',
        url: '/message/getting',
        contentType: 'application/json; charset=UTF-8',
        success: function (data_return) {
            var messages = eval(data_return);
            var len = messages.length;
            if(len===0){
                $('#show_message').append('<p>这还什么都没有</p>');
            }
            else{
                for(var i=0;i<len;i++){
                    add_message_list(messages[i]);
                }
            }

        }
    });
}