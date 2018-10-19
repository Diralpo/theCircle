function showTable(data, sort) {
    //根据传入的json数组创建表格
    var createTable = function(data) {
        //定义表格
        var table = document.createElement('table');
        //table.setAttribute('style', 'width: 450px;');
        table.setAttribute('class','theTable');
        //定义表格标题
        var caption = document.createElement('caption');
        caption.innerHTML = '查询结果';
        //将标题添加进表格
        table.appendChild(caption);

        var tr = document.createElement('tr');
        for (var i = 0; i < sort.length; i++) {
            var tdNo = document.createElement('th');
            tdNo.innerHTML = sort[i];
            tr.appendChild(tdNo);
        }
        var tdNo = document.createElement('th');
        tdNo.innerHTML = "";
        tr.appendChild(tdNo);
        table.appendChild(tr);

        //table.childNodes[1].setAttribute('style', 'background:#cae8ea;');
        for (var i = 0; i < data.length; i++) {
            var tr = document.createElement('tr');
            for (var j = 0; j < sort.length; j++) {
                var tdNo = document.createElement('td');
                tdNo.innerHTML = data[i][sort[j]];
                tr.appendChild(tdNo);
            }
            var tdNo = document.createElement('td');
            tdNo.innerHTML = "<button id=\"stu_query\" class=\"btn btn-default\" type=\"button\" onclick=\"test()\">查询</button>";
            tr.appendChild(tdNo);
            table.appendChild(tr);
        }
        return table;
    };

    var parent = document.getElementById("stu_form_fa");
    var child = document.getElementById("stu_query");
    parent.removeChild(child);
    child = document.getElementById("stu_form");
    parent.removeChild(child);

    var para = document.createElement("div");
    para.id = "stu_form";
    parent.appendChild(para);
    var body = document.getElementById("stu_form");
    body.appendChild(createTable(data));
}