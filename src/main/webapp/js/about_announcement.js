function pop_up_announcement() {
    var the_str = '<div class="container">' +
        '<div class="row">' +
        '<input type="text" class="form-control" id="input_ann_title" placeholder="公告标题">' +
        '<strong>公告内容</strong>' +
        '<textarea class="form-control" rows="10" id="insert_announcement_details"></textarea>' +
        '<button class="btn btn-success" onclick="sent_announcement()">发送公告</button>' +
        '</div></div>';
    $('.aw-container-wrap').css('display', 'none').after(the_str);

}

function sent_announcement() {
    var u_id = sessionStorage.getItem("u_id");
    if (u_id == null) {
        alert("请先登录");
        window.location.href = "login.html";
        return;
    }
    var gro_id = get_query_object(window.location.href)['gro_id'];
    var data = {
        'gro_id': gro_id,
        'details': $('#insert_announcement_details').val(),
        'u_id': u_id,
        'title': $('#input_ann_title').val()
    };
    // 待定
    $.ajax({
        type: 'POST',
        url: '/announcement/createAnnouncement',
        data: JSON.stringify(data),  //转化字符串
        contentType: 'application/json; charset=UTF-8',
        success: function (data_return) {
            var return_code = data_return['code'];
            if (return_code === 200) {
                alert("发送成功");
            }
            else {
                alert("系统出错");
            }
        }
    });
    $('.aw-container-wrap').css('display', '').next().remove();
}

function get_announcement() {
    var u_id = sessionStorage.getItem("u_id");
    if (u_id == null) {
        alert("请先登录");
        window.location.href = "login.html";
        return;
    }
    var gro_id = get_query_object(window.location.href)['gro_id'];
    var data = {'gro_id': gro_id, 'u_id': u_id};
    $.ajax({
        type: 'POST',
        url: '/announcement/getAnnouncement',
        data: JSON.stringify(data),  //转化字符串
        contentType: 'application/json; charset=UTF-8',
        success: function (data_return) {
            if (data_return['code'] === 400) {
                alert("系统出错，请重新登录");
                window.location.href = "login.html";
                return;
            }
            data_return = eval(data_return);
            var len = data_return.length;
            if (len > 0) {
                for (var i = 0; i < len; i++) {
                    var str = '<div class="aw-item " >' +
                        '<div class="aw-question-content">' +
                        '<h4><a >' +
                        data_return[i]['ann_title'] +
                        '</a></h4>' +
                        '<p>' +data_return[i]['ann_details']+
                        '</p>'+
                        '<p>' +
                        '<a class="aw-user-name" onclick="jump_to_user_index(\'' +
                        data_return[i]['u_nickname']+
                        '\')">' +
                        data_return[i]['u_nickname']+
                        '</a>' +
                        '<span class="text-color-999">发布了该公告  <p class="ann-time">' +
                        data_return[i]['ann_create_time']+
                        '</p> </span>' +
                        '</p> </div> </div>';
                    $('.aw-common-list').append(str)
                }
            }
        }
    });
}