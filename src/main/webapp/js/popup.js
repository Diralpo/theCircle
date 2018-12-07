
function pop(obj) {
    function pop_up(obj) {
        $('body').append('<div id="mry-opo"><div id="mry-opo-title"></div><div  id="mry-opo-content"></div></div>');
        var div = $('#mry-opo');
        $('#mry-opo-title').text(obj.title);
        $('#mry-opo-content').append(obj.content);
        div.css('width', obj.width + 'px');
        div.css('height', obj.height + 'px');
        div.css('margin-left', -(parseInt(obj.width) / 2) + 'px');
        div.css('margin-top', -(parseInt(obj.height) / 2) + 'px');
        div.css('background', obj.backgorund);
        $('#mry-mask').css('display', 'block');
    }

    function del() {
        $('#mry-opo').append('<a href="javascript:void(0)" deletes="mry-opo" style="position:absolute;right:10px;top:6px;color:#fff;font-size:12px;">X</a>');
        $('[deletes=mry-opo]').click(function () {
            $('#mry-opo,#mry-mask').remove();
        });
    }

    $('body').append('<div id="mry-mask" deletes="mry-opo"></div>');
    var ject = obj;
    ject.width = parseInt(obj.width) || 500;
    ject.height = parseInt(obj.height) || 500;
    ject.title = obj.title || '测试标题';
    ject.content = obj.content || '测试内容';
    ject.backgorund = obj.backgorund || '#fff';
    pop_up(ject);
    del();
}