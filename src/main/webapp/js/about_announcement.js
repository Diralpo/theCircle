function pop_up_announcement() {
    var the_str='<div class="container"><div class="row"><strong>公告内容</strong>' +
        '<textarea class="form-control" rows="10" id="insert_information_details"></textarea>' +
        '<button class="btn btn-success" onclick="sent_announcement()">发送公告</button></div></div>';
    $('.aw-container-wrap').css('display', 'none').after(the_str);

}

function sent_announcement(){
    var data = get_query_object(window.location.href);
    // 待定
    $.ajax({
        type: 'POST',
        url: '/',
        data: JSON.stringify(data),  //转化字符串
        contentType: 'application/json; charset=UTF-8',
        success: function (data_return) {
            var user_data = eval(data_return);
        }
    });
    $('.aw-container-wrap').css('display', '').next().remove()

}