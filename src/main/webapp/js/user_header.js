//该 js 用于对网页头部用户进行渲染，使用session,用户信息统一存储在session中
$(function() {
    var nickname = sessionStorage.getItem('nickname');
    console.log(nickname)
    if(nickname!=null){
        $('#unlogin').css('display','none')
        $('#login').css('display','block')
    }
    else {
        $('#unlogin').css('display','block')
        $('#login').css('display','none')
    }
})
