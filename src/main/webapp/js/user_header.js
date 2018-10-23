//该 js 用于对网页头部用户进行渲染，使用session,用户信息统一存储在session中
$(function() {
    sessionStorage.setItem('userName','mhx')
    var userName = sessionStorage.getItem('userName');
    console.log(userName)
    if(userName!=null){
        $('#unlogin').css('display','none')
        $('#login').css('display','block')
    }
    else {
        $('#unlogin').css('display','block')
        $('#login').css('display','none')
    }
})
