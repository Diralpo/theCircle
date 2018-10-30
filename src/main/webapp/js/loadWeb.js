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
                "</p><div><img alt=\"" +
                data[i]['obj_name'] +
                "\" class=\"img-responsive\" src=\"" +
                data[i]['obj_img_href']+
                "\"></div><div><p class=\"object_name\">" +
                data[i]['obj_name'] +
                "</p></div></a></article>");
            $("#show_object").append(newP);
        }
        $("#show_object>article.col-lg-3").mouseenter(function () {
                $(this).css('backgroundColor', 'rgba(140, 150, 152, 0.1)');
            }
        ).mouseleave(function () {
            $(this).css('backgroundColor', '');
        });
    }
}