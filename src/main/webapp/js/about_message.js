function sent_message() {
    var detail = $('#insert_information_details').val();
    if(detail!==''){
        var data = {'details': detail, 'receiver': $('#u_nickname').text()};
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

function pop_up_message(){
    var astr='<strong>私信内容</strong>' +
        '<textarea class="form-control" rows="10" id="insert_information_details"></textarea>' +
        '<button class="btn btn-success" onclick="sent_message()">发送信息</button>';
    pop({height:350,width:600,title: '输入信息内容', content:astr})
}