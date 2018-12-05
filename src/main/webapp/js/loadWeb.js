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
            var newP = $("<article class=\"col-lg-3\"><a class='button' onclick='jump_to_object("+data[i]["obj_id"]+")'>" +
                "<p class=\"object_id\">" +
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
        url: '/object/search_by_likeName',
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

function jump_to_object(id) {
    var data = {
        "object_id":id,
        "email": sessionStorage.getItem("email")
    };
    $.ajax({
        type: 'POST',
        url: '/object/search_by_id',
        data: JSON.stringify(data),  //转化字符串
        contentType: 'application/json; charset=UTF-8',
        success: function (data_return) { //成功的话，得到消息
            var object_data = eval(data_return);
            console.log("123");
            console.log(object_data[0]["obj_name"]);
            console.log(object_data[0]);
            sessionStorage.setItem("obj_id", object_data[0]["obj_id"]);
            sessionStorage.setItem("obj_name", object_data[0]["obj_name"]);
            sessionStorage.setItem("obj_href", object_data[0]["obj_href"]);
            sessionStorage.setItem("obj_img_href", object_data[0]["obj_img_href"]);
            sessionStorage.setItem("obj_type", object_data[0]["obj_type"]);
            sessionStorage.setItem("obj_status", object_data[0]["obj_status"]);

            window.location.href = "object_template.html";
        }
    });
}