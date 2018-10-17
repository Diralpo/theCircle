var startTime = function() {
    var today = new Date();
    var h = today.getHours();
    var m = today.getMinutes();
    var s = today.getSeconds();
    // add a zero in front of numbers<10
    h = checkTime(h);
    m = checkTime(m);
    s = checkTime(s);
    document.getElementById('theClock').innerHTML = h + ":" + m + ":" + s;
    setTimeout('startTime()', 500)
}

function checkTime(i) {
    if (i < 10) {
        i = "0" + i
    }
    return i
}
var load_nav = function(){
    var createNav = function(){
        var adiv = document.createElement('div');
        adiv.setAttribute('text-align', 'center');
        adiv.innerHTML =    "<a href=\"index.html\" style=\"font-size:18px;\">主页</a> | "+
                            "<a href=\"login.html\" style=\"font-size:18px;\">登录</a> | " +
                            "<a href=\"signup.html\" style=\"font-size:18px;\">注册</a> | " +
                            "<a href=\"query.html\" style=\"font-size:18px;\">查询</a> | " +
                            "<a href=\"model.html\" style=\"font-size:18px;\">模板</a>";
        return adiv
    };
    var theNav = document.getElementById("theNav");
    theNav.setAttribute('text-align', 'center');
    theNav.appendChild(createNav());
};