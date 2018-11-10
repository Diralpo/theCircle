String.prototype.format = function (args) {// 格式化字符串
    var result = this;
    if (arguments.length > 0) {
        if (arguments.length === 1 && typeof (args) === "object") {
            for (var key in args) {
                if (args[key] !== undefined) {
                    var reg = new RegExp("({" + key + "})", "g");
                    result = result.replace(reg, args[key]);
                }
            }
        }
        else {
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] !== undefined) {
                    var reg = new RegExp("({)" + i + "(})", "g");
                    result = result.replace(reg, arguments[i]);
                }
            }
        }
    }
    return result;
};

function loadObject(data, len) {
    if (len > 0) {
        for (var i = 0; i < len; i++) {
            var newP = $("<article class=\"col-lg-3\"><a href=\"" +
                data[i]['obj_href'] +
                "\"><p class=\"object_id\">" +
                data[i]['obj_id'] +
                "</p><div class=\"img_div\"><img alt=\"" +
                data[i]['obj_name'] +
                "\" class=\"img-responsive\" src=\"" +
                data[i]['obj_img_href']+
                "\"></div><div class='row'><div class=\"col-md-8\"><p class=\"object_name\">" +
                data[i]['obj_name'] +
                "</p></div><div class=\"col-md-4\"><p class=\"object_type\">" +
                data[i]['obj_type'] +
                "</p></div></div></a></article>");
            $("#show_object").append(newP);
        }
        $("#show_object>article.col-lg-3").mouseenter(function () {
                $(this).css('backgroundColor', 'rgba(140, 150, 152, 0.3)');
            }
        ).mouseleave(function () {
            $(this).css('backgroundColor', '');
        });
    }
}

function searchObject(theStr) {
    var data = {"keyword": theStr, "email": sessionStorage.getItem("email")};
    $.ajax({
        type: 'POST',
        url: '/object/search',
        data: JSON.stringify(data),  //转化字符串
        contentType: 'application/json; charset=UTF-8',
        success: function (data_return) { //成功的话，得到消息
            $("#show_object").html("");
            var data = eval(data_return);
            var len = data.length;
            if (len > 0) {
                loadObject(data, len);
            }
            else{
                $("#show_object").html("搜索结果为空");
            }
        }
    });
}