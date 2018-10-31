function searchObject(theStr) {
    var data = {"keyword": theStr, "email": sessionStorage.getItem("email")};
    $.ajax({
        type: 'POST',
        url: '/object/search',
        data: JSON.stringify(data),  //转化字符串
        contentType: 'application/json; charset=UTF-8',
        success: function (data_return) { //成功的话，得到消息
            var data = eval(data_return);
            var len = data.length;
            if (len > 0) {
                loadObject(data, len);
            }
        }
    });
}