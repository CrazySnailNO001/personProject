index.js$(function () {

    function getFilterWithName(url, id, name) {
        var filter = $("#" + id).val();
        if (filter != undefined && filter != "") {
            url = url + name + "=" + filter + '&';
        }
        return url;
    }

    function getDateFilter(url) {
        url = getFilterWithName(url, "date_start", "date_start");
        url = getFilterWithName(url, "date_end", "date_end");
        return url;
    }

    // 粮库查询
    $("#filter_warehouse_list").click(function () {
        var url = "/warehouse/list?";
        url = getFilterWithName(url, "filter_warehouse", "warehouse");
        url = getFilterWithName(url, "filter_province", "province");
        url = getFilterWithName(url, "filter_commune", "commune");
        url = getFilterWithName(url, "filter_county", "county");
        window.location.href = url;
    });

    // 粮库报价查询
    $("#filter_price_list").click(function () {
        var url = "/price/list?";
        url = getFilterWithName(url, "filter_warehouse", "warehouse");
        url = getFilterWithName(url, "filter_product", "product");
        url = getDateFilter(url);
        window.location.href = url;
    });

    // 消息列表查询
    $("#filter_message_list").click(function () {
        var url = "/message/list?";
        url = getFilterWithName(url, "filter_name", "name");
        url = getFilterWithName(url, "filter_mobile", "mobile");
        url = getFilterWithName(url, "filter_title", "title");
        url = getFilterWithName(url, "filter_content", "content");
        url = getFilterWithName(url, "filter_status", "status");
        window.location.href = url;
    });

    // 订单列表查询
    $("#filter_order_list").click(function () {
        var url = "/order/list?";
        url = getFilterWithName(url, "filter_warehouse", "warehouseName");
        url = getFilterWithName(url, "filter_product", "product");
        url = getFilterWithName(url, "filter_level", "level");
        url = getFilterWithName(url, "filter_customerMobile", "customerMobile");
        url = getFilterWithName(url, "filter_status", "status");
        url = getFilterWithName(url, "date_start", "date_start");
        url = getFilterWithName(url, "date_end", "date_end");
        url = getFilterWithName(url, "filter_orderNo", "orderNo");
        window.location.href = url;
    });

    // 客户列表查询
    $("#filter_user_list").click(function () {
        var url = "/user/list?";
        url = getFilterWithName(url, "filter_mobile", "mobile");
        url = getFilterWithName(url, "filter_name", "name");
        window.location.href = url;
    });

    // 微信客户列表查询
    $("#filter_wx_list").click(function () {
        var url = "/user/wx_list?";
        url = getFilterWithName(url, "filter_mobile", "mobile");
        url = getFilterWithName(url, "filter_name", "name");
        window.location.href = url;
    });

    // 用户查询
    $("#filter_operator_list").click(function () {
        var url = "/operator/list?";
        url = getFilterWithName(url, "filter_login_name", "loginName");
        url = getFilterWithName(url, "filter_real_name", "realName");
        url = getFilterWithName(url, "filter_status", "status");
        window.location.href = url;
    });

    // 银行信息查询
    $("#filter_bank_list").click(function () {
        var url = "/bank/list?";
        url = getFilterWithName(url, "filter_mobile", "mobile");
        url = getFilterWithName(url, "filter_name", "name");
        url = getFilterWithName(url, "filter_status", "status");
        window.location.href = url;
    });
});