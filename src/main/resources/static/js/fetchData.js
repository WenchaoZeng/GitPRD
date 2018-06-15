/**
 * Created by sober on 2018/6/11.
 */

/**
 *  初始化方法
 */
$(function () {
    var cookie = $.cookie('branchTitle');
    if (cookie != null && cookie != "") {
        getBranchList(cookie)
    } else {
        getBranchList("LOCAL")
    }
    //todo 同步显示按钮状态 根据cookie设置
    if (cookie == "ALL") {
        $("#switchBranchList").attr("checked", true)
    }else{
        $("#switchBranchList").attr("checked", false)
    }
})

/**
 *  拉取分支数据
 */

function switchBranchList() {
    var cookie = $.cookie('branchTitle');
    if (cookie != null) {
        if (cookie == "LOCAL") {
            getBranchList("ALL")
        } else {
            getBranchList("LOCAL")
        }
    }
}

function getBranchList(branchType) {
    // 拉取分值列表
    var json = {
        "branchListType": branchType
    };

    var result = requestApiGateway("/api/branches", json)

    if (result.result) {
        $("#branchListTitle").html("")
        if (branchType == "ALL") {
            $.cookie('branchTitle', 'ALL');
        } else if (branchType == "LOCAL") {
            $.cookie('branchTitle', 'LOCAL');
        }
        //alert(result.data)
        $("#branchTable").html("")

        $("#branchTable").append(
            "<tr class='active'>" +
            "<td><span class='glyphicon glyphicon-th-list' aria-hidden='true'></span>分支名</td>" +
            "<td><span class='glyphicon glyphicon-cog' aria-hidden='true'></span>操作</td> " +
            "<td><span class='glyphicon glyphicon-cloud' aria-hidden='true'></span>打开链接</td>" +
            "</tr>"
        )

        if (result.data.length == 0) {
            $("#branchTable").append("<a3>空空如也~</a3>");
            return
        }

        $.each(result.data, function (index, value) {
            $("#branchTable").append(
                "<tr>" +
                "<td>" + value.name + "</td>" +
                "<td>" +
                "<div id ='" + value.name + "' + class='btn-group' role='group' aria-label='...'>" +
                "<button type='button' class='btn btn-default' onclick= 'delBranch(" + JSON.stringify(value.name) + ")'>删除</button>&nbsp;" +
                "</div>" +
                "<div class='btn-group' role='group' aria-label='...'>" +
                "</div>" +
                "</td>" +
                "<td>" +
                "<u><a onclick='openLink()'>在线地址</a></u>&nbsp;" +
                "<u><a href=“#”>在Finder中打开</a></u>" +
                "</td>" +
                "</tr>"
            );

            if (value.type == "REMOTE") {
                $('#' + value.name).append(
                    "<button type='button' class='btn btn-default' onclick= 'fetchBranchToLocal(" + JSON.stringify(value.name) + " )' >拉取到本地</button>&nbsp;"
                )
            }

            if (value.type == "LOCAL") {
                $('#' + value.name).append(
                    "<button type='button' id='" + value.name +"reset"+ "' class='btn btn-default' onclick='resetLocalChange(" + JSON.stringify(value.brandName) + " )'>撤销本地更改</button>&nbsp;" +
                    "<button type='button' id='" + value.name +"pull"+ "' class='btn btn-default'  onclick='pullRemoteUpdate(" + JSON.stringify(value.brandName) + " )'>拉取更新</button>&nbsp;"
                )
            }
        })
    } else {
        // do something
        alert(result.msg)
    }

    // 更新按钮的状态
    var result = requestApiGateway("/api/get_all_branch_status", null);
    if(result.result) {
        $.each(result.data, function (index, value) {
            if (!value.hasUncommittedChanges) {
                $('#' + value.branchName + "reset").hide()
            } else {
                $('#' + value.branchName + "reset").show()
                //parent.location.reload()
            }

            if (!value.hasUnPulledChanges) {
               $('#' + value.branchName + "pull").hide()
            }else {
                $('#' + value.branchName + "pull").show()
            }
        })
    }else {
        alert(result.msg)
    }

}

/**
 *  删除分支
 */
function delBranch(branchName) {
    var json = {
        "branchName" : branchName
    }
    var result = requestApiGateway("/api/delete", json)
    if (result.result) {
        alert("分支名 : " + branchName + " 删除成功!");
        window.location.reload()
    }else {
        alert(result.msg)
    }
}

/**
 *  拉取分支到本地
 */
function fetchBranchToLocal(branchName) {
/*    bootbox.alert("拉取分支到本地成功!", function () {
                })*/
// todo 是否可以优化webView性能

    var json = {
        "branchName" : branchName
    }

    var result = requestApiGateway("/api/clone_branch", json)
    if (result.result) {
        alert("分支名 : " + branchName + " 拉取到本地成功!");
        window.location.reload()
    }else {
        alert(result.msg)
    }
}


/**
 *  模态框 /新建分支
 */
function newBranch(sourceBranch, targetBranch) {
    var json = {
        "refBranchName" : sourceBranch,
        "branchName" : targetBranch
    }
    var result = requestApiGateway("/api/create_branch", json)
    if (result.result) {
        // do something
        $("#sourceBranch").attr("value","");
        $("#targetBranch").attr("value","");
        $('#newBranchModal').modal('hide')
        // refresh
        window.location.reload()
    }else {
        alert(result.msg)
    }

}

function hideSettingModal() {
    $('#settingModal').modal('hide')
}

function showNewBranchModal() {
    $('#newBranchModal').modal('show')
}

/**
 *  post ajax request
 */
function requestApiGateway(url, json) {
    return result =  requestApiGateway2(url, json, "POST")
}

function requestApiGateway2(url, json, type) {
    return result = requestApiGateway3(url, json, type, "application/json; charset=utf-8", false)
}

function requestApiGateway3(url, json, type, contentType, async) {
    var finalResult = null;
      $.ajax({
        type : type,
        url: url,
        async : async,
        contentType : contentType,
        data : JSON.stringify(json),
        dataType : "json",
        success: function (result) {
           finalResult = result
        }
     })

    return finalResult
}
/**
 * 新窗口中打开地址
 */
function openLink() {
    window.open('http://www.waitsober.com', '_blank', 'toolbar=no,status=no,scrollbars=yes')
}

/**
 * 推送
 */
function noticeCallback(data) {
    console.log("fetch branch status ");

    if(data.type == "BRANCH_STATUS") {
        $.each(data.content, function (index,value) {
            if (!value.hasUncommittedChanges) {
                $('#'+value.branchName+"reset").hide()
            }else {
                $('#'+value.branchName+"reset").show()
                //parent.location.reload()
            }

            if (!value.hasUnPulledChanges) {
                $('#'+value.branchName+"pull").hide()
            } else {
                $('#'+value.branchName+"pull").show()
            }
        })
    }

    if(data.type == "MSG") {

    }
}
/**
 *  重置修改
 */
function resetLocalChange(name) {
    var json = {
        branchName : name
    }
    
    var result = requestApiGateway("/api/reset_modify", json)
    if (result.result) {
        alert("撤销本地修改成功")
        window.location.reload()
    }else {
        alert(result.msg)
    }
}
/**
 * 拉取远程分支更新
 */
function pullRemoteUpdate(name) {
    var json = {
        branchName : name
    }
    var result = requestApiGateway("/api/pull", json)
    if (result.result) {
        alert(name + " : 拉取远程更新成功!")
    }else {
        alert(result.msg)
    }
}
