$(function () {
    /** 下拉菜单移入自动展开 **/
    $('li.dropdown').mouseover(function () {
        $(this).addClass('open');
    }).mouseout(function () {
        $(this).removeClass('open');
    });
});