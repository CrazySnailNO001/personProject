var price_digit = 4;
var amount_digit = 2;

//region 新增表单
function toAddNew(name) {
    $(".modal-add #pop_title").html(name);
    $(".modal-add .relate-detail").hide();
    $(".modal-add .btn-save").text("保存");
    $(".modal-add .btn-save").removeClass("btn-modify").addClass("btn-add-new");
}

//endregion

// region 显示关闭按钮
function showCloseButon(name) {
    $(name).show();
}

//endregion

// region 判断 编辑/查看
function editOrView(action) {
    if (action == "edit") {
        $(".modal-add .btn-save").show();
        $(".modal-add .btn-save").text("修改");
        $(".modal-add .btn-save").removeClass("btn-add-new").addClass("btn-modify");

        $(".modal-add .relate-detail .button_group").show();
    } else if (action == "view") {
        $(".btn-close").show();
        $(".modal-add .btn-save").hide();
        $(".modal-add .relate-detail .button_group").hide();
    }
}

//endregion

// 退出
function dologout() {
    if (confirm("确定要退出？")) {
        $.ajax({
            url: "/logout",
            type: 'GET',
            contentType: "application/json",
            dataType: 'json',
            data: {},
            success: function () {
                window.location.href = "/login";
            },
            error: function (error, textStatus, errorThrown) {
                var responseJson = jQuery.parseJSON(error.text);
                showpop(responseJson.message);
            }
        });
    }
}

// region 弹出框
function popOpen(obj, dask, modal) {
    obj.click(function (event) {
        $("html,body").addClass("hidden");
        $("input[name='res']").click();
        dask.fadeIn();
        modal.slideDown();
        event.stopPropagation();
    });
}

function popClose(obj, dask, modal) {
    obj.click(function (event) {
        if (obj.is(event.target) && obj.has(event.target).length === 0) {
            $("html,body").removeClass("hidden");
            modal.slideUp();
            dask.fadeOut();
            event.stopPropagation();
        }
    });
}

function showpop(msg, time) {
    $(".pop").html(msg);
    $(".pop").show();
    if (time > 0) {
        setTimeout(hidepop, time);
    } else
        setTimeout(hidepop, 3000);
}

function hidepop() {
    $(".pop").html("");
    $(".pop").hide();
}

//endregion

// 全选
function allCheck(check, table) {
    $(check).click(function () {
        if ($(this).prop("checked")) {//全选
            $(table + ' input[type=checkbox]').prop('checked', true);
        } else {
            $(table + ' input[type=checkbox]').prop("checked", false)
        }
    });
}

// 单选
function singleCheck(box_id) {
    var Box = $(box_id);
    Box.click(function () {
        if (this.checked || this.checked == 'checked') {
            Box.removeAttr("checked");
            $(this).prop("checked", true);
        }
    });
}

// 允许修改表单
function enable(object, enable) {
    if (enable) {
        $(object).prop("disabled", false);
        $(object).css("cursor", "pointer");
    } else {
        $(object).prop("disabled", true);
        $(object).css("cursor", "not-allowed");
    }
}

function enableList(inputs, enable) {
    for (var i = 0; i < inputs.length; i++) {
        if (enable) {
            $(inputs[i]).prop("disabled", false);
            $(inputs[i]).css("cursor", "pointer");
        } else {
            $(inputs[i]).prop("disabled", true);
            $(inputs[i]).css("cursor", "not-allowed");
        }
    }
}

// 判断文本框是否为空
function isFormCompleted(form) {
    var inputs = $(form);
    for (var i = 0; i < inputs.length; i++) {
        if (inputs[i].type == "text" || inputs[i].type == "number") {
            if (inputs[i].value == "") {
                showpop("请填写完整！");
                return false;
            }
        }
    }
    return true;
}

function getUrlParameterString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)
        return r[2];
    return null;
}

function getUrlParameters() {
    var url = location.search;
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
        }
    }
    return theRequest;
}

//获取json格式数据
function getJsonFormula(form) {
    var json = {};
    var inputs = $(form).find("input.input,textarea.input,input.input-area");
    for (var i = 0; i < inputs.length; i++) {
        json[inputs[i].name] = inputs[i].value;
    }
    inputs = $(form).find("span.input");
    for (var i = 0; i < inputs.length; i++) {
        json[inputs[i].getAttribute("name")] = inputs[i].innerText;
    }
    inputs = $(form).find("select.select,select.input-area");
    for (var i = 0; i < inputs.length; i++) {
        json[inputs[i].name] = inputs[i].options[inputs[i].options.selectedIndex].value;
    }
    return json;
}

//region 数字格式
function unsignedNumber(th) {
    if (th.value.length == 1) {
        th.value = th.value.replace(/[^1-9]/g, '');
    } else {
        th.value = th.value.replace(/\D/g, '');
    }
}

function amountCheck(th, digit) {
    var pos = 2;
    if (digit != "") {
        pos = digit;
    }
    var regStrs = [
        ['^0(\\d+)$', '$1'], //禁止录入整数部分两位以上，但首位为0
        ['[^\\d\\.]+$', ''], //禁止录入任何非数字和点
        ['\\.(\\d?)\\.+', '.$1'], //禁止录入两个以上的点
        ['^(\\d+\\.\\d{' + pos + '}).+', '$1'] //禁止录入小数点后两位以上
    ];
    for (i = 0; i < regStrs.length; i++) {
        var reg = new RegExp(regStrs[i][0]);
        th.value = th.value.replace(reg, regStrs[i][1]);
    }
}

function formatFloat(src, digit) {
    var pos = 2;
    if (digit != "") {
        pos = digit;
    }
    amountCheck(src, digit);
    if (src.value != "") {
        src.value = Math.round(src.value * Math.pow(10, pos)) / Math.pow(10, pos);
    }
}

//endregion

var slimScrollNav = function (obj, width, height) {
    obj.slimScroll({
        width: width,
        height: height,
        opacity: .6,
        distance: '5px',
        color: '#879bab',
        alwaysVisible: false,
        railVisible: true,
        railColor: '#e7e7e7',
        railOpacity: .4,
        borderRadius: '5px',
        size: '5px',
        disableFadeOut: false
    });
};

function ctrlMenu(menuId, pageId) {
    var menu = document.getElementById(menuId);
    menu.classList.add("active");
    menu.getElementsByClassName("navList")[0].setAttribute("style", "display:block;");
    $(".aside-menu").find("#" + pageId).addClass("active");

    var liHeight = 34;
    var el = menu.getElementsByClassName("navList")[0];
    if (el.offsetHeight > (liHeight * 4 - 15)) {
        slimScrollNav($(el), "auto", (liHeight * 4 - 15));
    }
    resizeMenuHeight();
}

var resizeMenuHeight = function () {
    if ($(window).height() - $('.zl-logo').outerHeight(true) < $('.subNavContent').outerHeight(true)) {
        slimScrollNav($('.aside-menu'), '230px', 'auto');
    }
};

//region ajax
function ajaxError(error) {
    if (error.status == 401) {
        window.location.href = "/login";
    } else {
        showpop(jQuery.parseJSON(error.responseText).exception);
    }
}

function ajax(url, data, success, type, dataType, error) {
    var type = type || 'post';//请求类型
    var dataType = dataType || 'json';
    var contentType = "application/json";
    if (url.indexOf("delete") != -1 || url.indexOf("resetPassword") != -1 || type == "get") {
        contentType = "application/x-www-form-urlencoded; charset=UTF-8";
    }
    var success = success || function (data) {
    };
    var error = error || function (error, textStatus, errorThrown) {
        ajaxError(error);
    };

    $.ajax({
        'url': url,
        'data': data,
        'type': type,
        'contentType': contentType,
        'dataType': dataType,
        'success': success,
        'error': error
    });
}

// submitAjax(post方式提交)
function submitAjax(form, success) {
    cache = cache || true;
    var form = $(form);
    var url = form.attr('action');
    var data = form.serialize();
    ajax(url, data, success, 'post', 'json');
}

// ajax提交(post方式提交)
function post(url, data, success, error) {
    ajax(url, data, success, 'post', 'json', error);
}

// ajax提交(get方式提交)
function get(url, success) {
    ajax(url, {}, success, 'get', 'json');
}

//endregion

//region date
function initLaydate() {
    lay('.laydate-icon:not(.date-max)').each(function () {
        laydate.render({
            elem: this
            , trigger: 'click'
        });
    });

    lay('.laydate-max').each(function () {
        laydate.render({
            elem: this
            , max: formatDate(new Date())
            , trigger: 'click'
        });
    });

    lay('.laydate-year').each(function () {
        laydate.render({
            elem: this
            , type: 'year'
            , change: function (value) {
                $(this).get(0).elem.val(value);
            }
        });
    });
}

function dateAdd(strInterval, Number) {
    var dtTmp = new Date();
    switch (strInterval) {
        case 's' :
            return new Date(Date.parse(dtTmp) + (1000 * Number));
        case 'n' :
            return new Date(Date.parse(dtTmp) + (60000 * Number));
        case 'h' :
            return new Date(Date.parse(dtTmp) + (3600000 * Number));
        case 'd' :
            return new Date(Date.parse(dtTmp) + (86400000 * Number));
        case 'w' :
            return new Date(Date.parse(dtTmp) + ((86400000 * 7) * Number));
        case 'q' :
            return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number * 3, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
        case 'm' :
            return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
        case 'y' :
            return new Date((dtTmp.getFullYear() + Number), dtTmp.getMonth(), dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
    }
}

var formatDate = function (date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? '0' + m : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    return y + '-' + m + '-' + d;
};

//endregion

function bindFilter() {
    var btn_search = $(".aside-menu-box-search .btn");
    $(".search-box").unbind('keyup').removeAttr('keyup').bind('keyup', function (event) {
        if (event.keyCode == "13") {
            btn_search.click();
        }
    });

    $(".search-box input").change(function () {
        btn_search.click();
    });

    $(".search-box select").unbind('change').removeAttr('change').bind("change", function (e) {
        btn_search.click();
    });
}

$(function () {
    $(".userbar .logout").click(function () {
        dologout();
    });

    //region 导航
    var subNavHeader = {};
    subNavHeader.init = function () {
        var liHeight = 34;

        $(".subNavHeader").click(function (event) {
            if (!$(this).parent('.subNavCon').hasClass('active')) {
                $('.subNavCon').removeClass('active');
                $(".navList").slideUp(300);
                $('.subNavCon').find(".slimScrollDiv").slideUp(300);
                $(this).parent('.subNavCon').addClass('active');
                var DomLength = $(this).parent('.subNavCon').find('.slimScrollDiv').length;
                var MaxHeight = DomLength === 0 ? $(this).siblings(".navList").height() : $(this).next(".slimScrollDiv").children('.navList').height();

                if (DomLength === 1) {
                    $(this).next(".slimScrollDiv").slideDown(300);
                    $(this).next(".slimScrollDiv").children('.navList').slideDown(300);
                } else {
                    $(this).next(".slimScrollDiv").slideUp(300);
                }
                if (MaxHeight > (liHeight * 4 - 15)) {
                    slimScrollNav($(this).next(".navList"), "auto", (liHeight * 4 - 15));
                    $(this).next(".slimScrollDiv").slideDown(300);
                    $(this).next(".slimScrollDiv").children('.navList').slideDown(300);
                } else {
                    $(this).next(".navList").slideDown(300);
                }

            } else {
                $('.subNavCon').removeClass('active');
                $(this).next(".slimScrollDiv").slideUp(300);
                $(".navList").slideUp(300);
            }
        });

        var asideMenuWidth = $(".aside-menu").outerWidth();
        var displayArrowWidth = $(".displayArrow").outerWidth();
        $('.displayArrow').click(function () {
            if ($(this).children('.pngfix').hasClass('close')) {
                $(".aside-menu").animate({
                    marginLeft: '0'
                });
                $(".aside-menu-box").animate({
                    left: displayArrowWidth + asideMenuWidth + "px"
                });
                $(this).animate({
                    left: '0'
                }).children('.pngfix').removeClass('close')
            } else {
                $(".aside-menu").animate({
                    marginLeft: -asideMenuWidth + "px"
                });
                $(".aside-menu-box").animate({
                    left: displayArrowWidth + "px"
                });
                $(this).animate({
                    left: '0',
                    display: 'block'
                }).children('.pngfix').addClass('close')
            }
        });
    };
    subNavHeader.init();
    $(window).resize(function () {
        resizeMenuHeight();
    });
    //endregion

    // 滚动条样式
    slimScrollNav($(".dropdown-ul"), "auto", "100%");
    // pop
    slimScrollNav($(".modal-wrapper1"), "auto", "auto");
    //企业管理pop
    slimScrollNav($("#HBoxMessage .HBoxListMargin"), "auto", 500);

    //region 勾选下拉菜单
    var dropdownList = {};
    dropdownList.init = function () {
        var fold = true;
        $(".dropdown-wrapper .btn-dropdown-toggle").click(function (event) {
            if (fold) {
                $(this).siblings(".dropdown-div").show();

            } else {
                $(this).siblings(".dropdown-div").hide();
            }
            fold = !fold;
            event.stopPropagation();
        });

        // 订单列表选择查看
        $(".dropdown-div").click(function (event) {
            event.stopPropagation();
        });
        $(".dropdown-div .dropdown-ul li").click(function (event) {
            var dropdownLiLength = $(".dropdown-div .dropdown-ul li").length;
            var tabRowLength = $(".tab-cgdd-list tr th").length;
            var index1 = dropdownLiLength === tabRowLength ? $(this).index() : $(this).index() + 1;
            if ($(this).find("input[type=checkbox]").is(":checked")) {
                $(".tab-cgdd-list tr td").each(function () {
                    if ($(this).index() == index1) {
                        $(this).show();
                    }
                });
                $(".tab-cgdd-list tr th").eq(index1).show();
            } else {
                $(".tab-cgdd-list tr td").each(function () {
                    if ($(this).index() == index1) {
                        $(this).hide();
                    }
                });
                $(".tab-cgdd-list tr th").eq(index1).hide();
            }
            event.stopPropagation();
        });
        $(document).click(function (event) {
            $('.dropdown-div').hide();
            fold = true;
        });
    };
    dropdownList.init();
    //endregion

    //region 开关
    var powerSwitch = {};
    powerSwitch.init = function () {
        $("[class^=toggle]").click(function () {
            var that = $(this);
            if ($(this).hasClass("switch-disabled")) {
                return;
            }
            if ($(this).hasClass("toggle--on")) {
                $(this).removeClass("toggle--on").addClass("toggle--off").addClass('toggle--moving');
            } else {
                $(this).removeClass("toggle--off").addClass("toggle--on").addClass('toggle--moving');
            }
            setTimeout(function () {
                that.removeClass('toggle--moving');
            }, 200)
        });
        window.switchEvent = function (ele, on, off) {

            if ($(ele).hasClass("switch-disabled")) {
                return;
            }
            if ($(ele).hasClass('toggle--on')) {
                if (typeof on == 'function') {
                    on();
                }
            } else {
                if (typeof off == 'function') {
                    off();
                }
            }
        }
    };
    powerSwitch.showOn = function (ele) {
        $(ele).removeClass("toggle--off").addClass("toggle--on");
    };
    powerSwitch.showOff = function (ele) {
        $(ele).removeClass("toggle--on").addClass("toggle--off");
    };
    powerSwitch.init();
    //endregion

    //region 弹窗
    popOpen($(".aside-menu-box-list-title .btn-add-new"), $(".modal-backdrop"), $(".modal-add"));
    popClose($(".btn-close"), $(".modal-backdrop"), $(".modal"));
    popClose($(".modal-backdrop"), $(".modal-backdrop"), $(".modal"));
    popOpen($(".tab-cgdd-list .btn-detail"), $(".modal-backdrop"), $(".modal-add"));
    popOpen($(".tab-cgdd-list .btn-edit"), $(".modal-backdrop"), $(".modal-add"));

    // 关闭弹窗数据清零
    $(".modal-add").find(".btn-close").click(function () {
        if (window.location.href.toString().indexOf("warehouse") < 0) {
            $(".modal-add .relate-detail").hide();
        }
        window.location.href = window.location.href.replace("add", "order_id");
    });
    $(".modal-backdrop").click(function () {
        if (window.location.href.toString().indexOf("warehouse") < 0) {
            $(".modal-add .relate-detail").hide();
        }
        window.location.href = window.location.href.replace("add", "order_id");
    });
    //endregion

    bindFilter();
});