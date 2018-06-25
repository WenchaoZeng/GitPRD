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
                "<span style='float: left;margin-top: -5px'> " +
                "<a class='red button float_a' style='font-style: inherit; color : #FBFBFF; ' onclick= 'delBranch(" + JSON.stringify(value.name) + ")'>删除</a> </span>" +
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
                    " <a  id='" + value.name + "pull" + "'  class='blue button float_a' style='font-style: inherit; color : #FBFBFF; ' onclick='pullRemoteUpdate(" + JSON.stringify(value.name) + " )'>拉取更新</a> </span>"   +

                    "<span style='float: right;margin-top: -5px'>" +
                    " <a  id='" + value.name + "commit" + "'  class='blue button float_a' style='font-style: inherit; color : #FBFBFF; ' onclick='commitChange(" + JSON.stringify(value.name) + " )'>提交改动</a> </span>"
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
 *  提交改动
 */
function commitChange(branchName) {
    var json = {
        "branchName": branchName
    }
    var result = requestApiGateway("/api/commit_modify", json)
    if (result.result) {
        alert("分支名 : " + branchName + " 本地修改提交成功!!");
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
function openLink(name) {
    var json = {
        branchName : name
    }
   var result = requestApiGateway("/api/get_online_url",json)
    if (!result.result) {
        alert(result.msg)
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
        alert(result.msg)
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
    var flag = confirm("拉取远程更新将丢失本地的更改, 是否继续?")
    if (flag) {
        var json = {
            branchName: name
        }
        var result = requestApiGateway("/api/pull", json)
        if (result.result) {
            alert(name + " : 拉取远程更新成功!")
        } else {
            alert(result.msg)
        }
    }
}
