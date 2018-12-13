function search_group_by_uni() {
    var data={
        'obj_id':sessionStorage.getItem("obj_id"),
        "uni_name":$("#uni_name").val()
    }
    $.ajax({
        type: 'POST',
        url: '/group/search_group_by_uni',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=UTF-8',
        success: function (data_return) {
            $("#show_group_list").empty();
            var groupJson = eval(data_return);
            var len = groupJson.length;
            if(len==0){
                $("#group_list").append($("<p style=\"margin-top: 10px\">无结果</p>"));
            }else{
                for(var i=0;i<len;i++){
                    var groupId = groupJson[i]["gro_id"];
                    var content = $(
                        "<a href=\""+"group.html?"+param({"gro_id":groupId})+"\" class=\"list-group-item\">"+groupJson[i]["gro_name"]+"-"+groupJson[i]["uni_name"]+"</a>"
                    );
                    $("#show_group_list").append(content);

                }
            }
        }
    });
}


function create_group() {
    var u_id = sessionStorage.getItem("u_id");
    if(u_id==null){
        alert("请先登录");
        return;
    }
    var gro_obj_name = $("#group_object_name").val();
    var gro_manager_id = u_id;
    var gro_uni_name = $("#group_uni_name").val();
    var gro_name = $("#group_name");
    var data = {
        "u_id":u_id,
        "gro_manager_id":gro_manager_id,
        "gro_obj_name":gro_obj_name,
        "gro_uni_name":gro_uni_name,
        "gro_name":gro_name,
        "gro_detail":$("#group_introduction").val()
    }
    $.ajax({
        type: 'POST',
        url: '/group/create_group',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=UTF-8',
        success: function (data_return) {
            var code_return = eval(data_return);
            if(code_return["code"]==200){
                alert("申请注册成功，请等待审核")
            }else{
                alert("服务器后台错误")
            }
        }
    });
}

function add_group_focus(){
    var gro_id = get_query_object(window.location.href)["gro_id"];
    var u_id = sessionStorage.getItem("u_id");
    if(u_id==null){
        alert("请先登录");
        return;
    }
    var data={
      "gro_id":gro_id,
      "u_id":u_id
    };
    $.ajax({
        type: 'POST',
        url: '/group/add_group_focus',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=UTF-8',
        success: function (data_return) {
            var datares = eval(data_return);
            if(datares['code']===400){
                alert("系统出错");
            }
            else
                $('#add-group-focus').attr('onclick', 'remove_group_focus()').text('退出小组').attr("class", "btn btn-warning");

        }
    });

}

function remove_group_focus(){
    var gro_id = get_query_object(window.location.href)["gro_id"];
    var u_id = sessionStorage.getItem("u_id");
    if(u_id==null){
        alert("请先登录");
        return;
    }
    var data={
        "gro_id":gro_id,
        "u_id":u_id
    };
    $.ajax({
        type: 'POST',
        url: '/group/remove_group_focus',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=UTF-8',
        success: function (data_return) {
            var datares = eval(data_return);
            if(datares['code']===400){
                alert("系统出错");
            }
            else if(datares['code']===200)
                $('#add-group-focus').attr('onclick', 'add_group_focus()').text('+加入小组').attr("class", "btn btn-primary");
        }
    });

}

function refresh_announce_list() {
    var gro_id = get_query_object(window.location.href)["gro_id"];
    var data={
        "gro_id":gro_id
    }
    $.ajax({
        type: 'POST',
        url: '/announcement/get_announce_list',
        data: JSON.stringify(data),
        contentType: 'application/json; charset=UTF-8',
        success: function (data_return) {
            var datares = eval(data_return);
            for(var i=0;i<datares.length;i++){
                var content = $(" <div class=\"aw-item \"> <div class=\"aw-question-content\"> <h4>"+
                    "<a href=\"javascript:void(0)\" onclick=\"showContent("+datares[i]["ann_id"]+")\">"+datares[i]["ann_title"]+"</a> </h4> <p>"+
                    "<a class=\"aw-user-name\" >"+datares[i]["u_nickname"]+"</a>"+
                    "<span class=\"text-color-999\">发布了该公告 •"+datares[i]["ann_create_time"]+"</span> </p> </div> </div>"
                );
                console.log(content);
                $("#c_all_list").append(content);
            }
        }
    });

}