//region url
var $warehouse_url = "/warehouse/getCategory?";
var $user_url = "/user/getCategory?";
//endregion

//region type
var $type_warehouse = "warehouse";
var $type_user = "user";
//endregion

// 关联搜索弹出框
var hdialog = function ($url, $type, $elopen, $hide_id, $elbox, $width, $form) {
    if ($url[$url.length - 1] != '?' && $url[$url.length - 1] != '&')
        $url += '&';
    $elopen.hDialog({
        box: $elbox,
        width: $width,
        height: 'auto',
        positions: 'center',
        triggerEvent: 'click',
        effect: 'fadeOut',
        closeHide: false,
        resetForm: false,
        modalHide: true,
        escHide: false,
        beforeShow: function () {
            goPage($type, "#" + $elbox.attr("id"), $url, 1);
        }
    });
    $elbox.find(".btn-add-new").unbind('click').removeAttr('onclick').click(function () {
        setSelectedItem($type, $elbox, $elopen, $hide_id, $form);
        $elopen.hDialog('close', {box: $elbox})
    });
    $elbox.find(".dialog-close").unbind('click').removeAttr('onclick').click(function () {
        $elopen.hDialog('close', {box: $elbox})
    });
    $elbox.find(".btn-search").unbind('click').removeAttr('onclick').click(function () {
        filterItem($url, $type, $elbox);
    });

    $elbox.find(".input-area").unbind('keyup').removeAttr('keyup').bind('keyup', function (event) {
        if (event.keyCode == "13") {
            filterItem($url, $type, $elbox);
        }
    });
};

//region init list data
function setWarehouseListData(data) {
    var tableStr = "<tr><th></th><th>粮库名称</th><th>省份</th><th>地级市</th><th>区县</th></tr>";
    $.each(data.warehouse_list, function (i, v) {
        tableStr += "<tr><td><input type='checkbox' class='checkbox'></td>" +
            "<td class='warehouse'>" + v.warehouse + "</td>" +
            "<td class='province'>" + v.province + "</td>" +
            "<td class='commune'>" + v.commune + "</td>" +
            "<td class='county'>" + v.county + "</td>" +
            "<td hidden><input class='id' value='" + v.id + "'/></td></tr>";
    });
    return tableStr;
}

function setUserMobileAndNameListData(data) {
    var tableStr = "<tr><th></th><th>客户姓名</th><th>客户手机号</th></tr>";
    $.each(data.customer_list, function (i, v) {
        tableStr += "<tr><td><input type='checkbox' class='checkbox'></td>" +
            "<td class='name'>" + v.name + "</td>" +
            "<td class='mobile'>" + v.mobile + "</td>" +
            "<td hidden><input class='id' value='" + v.id + "'/></td></tr>";
    });
    return tableStr;
}

//endregion

function setPageData(type, pageParams, baseUrl, box_id) {
    var params = "\"" + type + "\"" + ',' + "\"" + box_id + "\"" + ',' + "\"" + baseUrl + "\"";
    var pageStr = "";
    var noData = "";
    switch (type) {
        case $type_warehouse:
            noData = "仓库";
            break;

        case $type_user:
            noData = "客户";
            break;
    }
    if (pageParams.total == 0) {
        pageStr += "<span class='zeropage'>没有相关" + noData + "</span>";
        return pageStr;
    } else if (pageParams.total > 0) {
        var firstPageStr = "<span class='firstpage'><a onclick='goPage(" + params + "," + 1 + ")'>&nbsp;</a></span>";
        var activePage = "<span class='page_active'>";
        var lastPage = "";
        if (pageParams.page > 1) {
            firstPageStr += "<span class='prevpage'><a onclick='goPage(" + params + "," + (pageParams.page - 1) + ")' class='disabled'>&nbsp;</a></span>"
        }
        var ii;
        var jj;
        if (pageParams.totalPage <= 10) {
            ii = 1;
            jj = pageParams.totalPage;
        } else {
            if ((pageParams.totalPage - pageParams.page ) <= 7) {
                ii = pageParams.totalPage - 9;
                jj = pageParams.totalPage;
            } else {
                if (pageParams.page >= 3) {
                    ii = pageParams.page - 2;
                    jj = pageParams.page + 7;
                } else {
                    ii = 1;
                    jj = 10;
                }
            }
        }
        var tmp = "";
        for (var i = ii; i <= jj; i++) {
            if (i == pageParams.page) {
                tmp += "<a onclick='goPage(" + params + "," + i + ")' class='page-select'>" + i + "</a>";
            } else {
                tmp += "<a onclick='goPage(" + params + "," + i + ")'>" + i + "</a>";
            }
        }
        activePage += tmp + "</span>";

        if (pageParams.page < pageParams.totalPage) {
            lastPage = "<span class='nextpage'><a onclick='goPage(" + params + "," + (pageParams.page + 1) + ")' >&nbsp;</a>" +
                "</span><span class='lastpage'><a onclick='goPage(" + params + "," + pageParams.totalPage + ")'>&nbsp;</a></span>";
        } else {
            lastPage = "<span class='lastpage'><a onclick='goPage(" + params + "," + pageParams.totalPage + ")' class='disabled'>&nbsp;</a></span>";
        }
        pageStr = firstPageStr + activePage + lastPage;
        return pageStr;
    }
}

function goPage($type, box_id, url, page) {
    $.ajax({
        url: url,
        data: {page: page},
        success: function (data) {
            var tableStr = "";
            var pageStr = "";
            var $elbox = $(box_id);
            switch ($type) {
                case $type_warehouse:
                    tableStr = setWarehouseListData(data);
                    pageStr = setPageData($type, data.warehouse_page, url, box_id);
                    break;

                case $type_user:
                    tableStr = setUserMobileAndNameListData(data);
                    pageStr = setPageData($type, data.customer_page, url, box_id);
                    break;
                default:
                    break;
            }
            $elbox.find(".input-area").select();
            $elbox.find(".boxTable").html(tableStr);
            $elbox.find(".paging_content").html(pageStr);
            singleCheck(".boxTable input:checkbox");
            $elbox.off("click");
            $elbox.on("click", "tr", function (e) {
                if (e.target.type === 'checkbox') {
                    return;
                }
                var input = $(this).find(":checkbox");
                if ($(input).prop("checked")) {
                    $(input).removeAttr("checked");
                } else {
                    $elbox.find(":checkbox").each(function () {
                        $(this).removeAttr("checked");
                    });
                    $(input).prop("checked", true);
                }
            });
        },
        error: function (error, textStatus, errorThrown) {
            showpop(jQuery.parseJSON(error.responseText).exception);
        }
    });
}

function filterItem($url, $type, $elbox) {
    var url = "";
    var filter = $elbox.find(".input-area").val();
    if (filter != undefined && filter != "") {
        switch ($type) {
            case $type_warehouse:
                url = $url + "warehouse=" + filter + '&';
                break;

            case $type_user:
                url = $url + "mobile=" + filter + '&';
                break;
        }
    }
    goPage($type, "#" + $elbox.attr("id"), url, 1);
}

function setSelectedItem($type, $elbox, $elopen, $hide_id, $form) {
    var checked = $elbox.find(".boxTable input:checkbox:checked");
    if (checked != undefined) {
        switch ($type) {
            case $type_warehouse:
                $elopen.val(checked.parent().parent().find("td.warehouse").text());
                $hide_id.val(checked.parent().parent().find("input.id").val());
                break;

            case $type_user:
                $elopen.val(checked.parent().parent().find("td.name").text());
                $hide_id.val(checked.parent().parent().find("input.id").val());
                break;
        }
        $elopen.change();
    }
}