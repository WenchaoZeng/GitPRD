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
        //slideNotification(result.data)
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
                "<span style='float: left;margin-top: -5px'> " +
                "<a class='red button float_a' style='font-style: inherit; color : #FBFBFF; ' onclick= 'showDeleteModal(" + JSON.stringify(value.name) + ")'>删除</a> </span>" +
                "</div>" +
                "<div class='btn-group' role='group' aria-label='...'>" +
                "</div>" +
                "</td>" +
                "<td>" +
                "<u><a onclick='openLink(" + JSON.stringify(value.name) + " )'>在线地址</a></u>&nbsp;" +
                "<u><a onclick='openInFinder(" + JSON.stringify(value.name) + " )'>在Finder中打开</a></u>" +
                "</td>" +
                "</tr>"
            );

            if (value.type == "REMOTE") {
                $('#' + value.name).append(
                    "<span style='float: right;margin-top: -5px'> " +
                    "<a class='blue button float_a' style='font-style: inherit; color : #FBFBFF; 'onclick= 'fetchBranchToLocal(" + JSON.stringify(value.name) + " )' >拉取到本地</a> </span>"
                )
            }

            if (value.type == "LOCAL") {
                $('#' + value.name).append(
                    "<span style='float: right;margin-top: -5px'> " +
                    "<a  id='" + value.name + "reset" + "'  class='blue button float_a' style='font-style: inherit; color : #FBFBFF; ' onclick='resetLocalChange(" + JSON.stringify(value.name) + " )' >撤销本地更改</a> </span>" +

                    "<span style='float: right;margin-top: -5px'>" +
                    " <a  id='" + value.name + "pull" + "'  class='blue button float_a' style='font-style: inherit; color : #FBFBFF; ' onclick='showConfirmModal(" + JSON.stringify(value.name) + " )'>拉取更新</a> </span>"   +

                    "<span style='float: right;margin-top: -5px'>" +
                    " <a  id='" + value.name + "commit" + "'  class='blue button float_a' style='font-style: inherit; color : #FBFBFF; ' onclick='openCommitWindow(" + JSON.stringify(value.name) + " )'>提交改动</a> </span>"
                )
            }

            if (value.status != null && !value.status.hasUncommittedChanges) {
                $('#' + value.name + "reset").hide()
                $('#' + value.name + "commit").hide()
            } else if(value.status != null && value.status.hasUncommittedChanges){
                $('#' + value.name + "reset").show()
                $('#' + value.name + "commit").show()
                //parent.location.reload()
            }

            if (value.status != null && !value.status.hasUnPulledChanges) {
                $('#' + value.name + "pull").hide()
            } else if(value.status != null && value.status.hasUnPulledChanges){
                $('#' + value.name + "pull").show()
            }

        })
    } else {
        // do something
        slideNotification(result.msg, "ERROR")
    }
}

/**
 *  删除分支
 */
function delBranch(branchName) {
    $('#deleteModal').modal('hide')

    var json = {
        "branchName" : branchName
    }
    var result = requestApiGateway("/api/delete", json)
    if (result.result) {
        slideNotification("分支名 : " + branchName + " 删除成功!", "INFO")
    }else {
        slideNotification(result.msg, "ERROR")
    }
}

/**
 *  拉取分支到本地
 */
function fetchBranchToLocal(branchName) {
    var json = {
        "branchName" : branchName
    }

    var result = requestApiGateway("/api/clone_branch", json)
    if (result.result) {
        slideNotification("分支名 : " + branchName + " 拉取到本地成功!", "INFO")
    }else {
        slideNotification(result.msg, "ERROR")
    }
}

/**
 *  提交改动
 */
function commitChange() {
    $('#commitModal').modal('hide')

    var branchName = $('#commitBranchName').val()
    var comment = $('#commitMsg').val()
    var json = {
        "branchName": branchName,
        "comment" : comment
    }
    var result = requestApiGateway("/api/commit_modify", json)
    if (result.result) {
        slideNotification("分支名 : " + branchName + " 提交本地修改成功!", "INFO")
    }else {
        slideNotification(result.msg, "ERROR")
    }
}


/**
 *  模态框 /新建分支
 */
function newBranch(sourceBranch, targetBranch) {
    $('#newBranchModal').modal('hide')

    var json = {
        "refBranchName" : sourceBranch,
        "branchName" : targetBranch
    }
    var result = requestApiGateway("/api/create_branch", json)
    if (result.result) {
        // do something
        $("#sourceBranch").attr("value","");
        $("#targetBranch").attr("value","");
        // refresh
        slideNotification("分支名 : " + targetBranch + " 新建成功!", "INFO")
    }else {
        slideNotification(result.msg, "ERROR")
    }
}

/**
 *  显示确认拉取模态框
 */
function showConfirmModal(branchName) {
    $('#confirmModal').modal('show')
    $('#confirmBranch').val(branchName)
}

/**
 *  显示删除模态框
 */
function showDeleteModal(branchName) {
    $('#deleteModal').modal('show')
    $('#deleteBranch').val(branchName)
    $('#deleteLabel').text("确认要删除 : " + branchName + " 分支吗?")
}

/**
 *  模态框 /提交改动
 */
function openCommitWindow(brandName) {
    $('#commitModal').modal('show')
    $('#commitBranchName').val(brandName)
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
function openLink(name) {
    var json = {
        branchName : name
    }
   var result = requestApiGateway("/api/get_online_url",json)
    if (!result.result) {
        slideNotification(result.msg, "ERROR")
    }
}

/**
 *  Finder打开
 */
function openInFinder(name) {
     var json = {
        branchName : name
    }
   var result = requestApiGateway("/api/open_in_finder",json)
    if (!result.result) {
        slideNotification(result.msg, "ERROR")
    }
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
                $('#'+value.branchName+"commit").hide()
            }else {
                $('#'+value.branchName+"reset").show()
                $('#'+value.branchName+"commit").show()
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
        // do nothing
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
        slideNotification("撤销本地修改成功", "INFO")
    }else {
        slideNotification(result.msg, "ERROR")
    }
}
/**
 * 拉取远程分支更新
 *
 */
function pullRemoteUpdate(name) {
    $('#confirmModal').modal('hide')

    var json = {
        branchName: name
    }
    var result = requestApiGateway("/api/pull", json)
    if (result.result) {
        slideNotification("拉取远程更新成功!", "INFO")
    } else {
        slideNotification(result.msg, "ERROR")
    }
}


/**
 * 通知
 */
function slideNotification(msg, level) {
    if (level == "ERROR") {
        $('#top_notification').css("background-color", "#d51b08");
    }else {
        $('#top_notification').css("background-color", "#5CB85C");
    }

    $('#notification_label').text(msg)
    relocation()
    $('#top_notification').slideDown(400);
    setTimeout(upNotification, 1200)
    setTimeout(reloadView, 1700)
    //window.location.reload()
}

function upNotification() {
    $("#top_notification").slideUp(400);
}

function reloadView() {
     window.location.reload()
}

/**
 *  点击回到页面顶部
 */
function relocation() {
     $("html,body").animate({scrollTop:0}, 500);
}
