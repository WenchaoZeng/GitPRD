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
    }else {
        getBranchList("LOCAL")
    }
})

    //location.reload(true)

/**
 *  拉取分支数据
 */

function switchBranchList() {
    var title = $("#branchListTitle").text()
    if(title == "本地分支") {
        getBranchList('ALL')
    }else if (title == "全部分支") {
        getBranchList('LOCAL')
    }

}

function getBranchList(branchType) {
    $.post("/api/getBranchList", {type: branchType}, function (result) {
        if (result.result) {
            $("#branchListTitle").html("")
            if (branchType == "ALL") {
                $("#branchListTitle").html("全部分支")
                $.cookie('branchTitle', 'ALL');
            }else if(branchType == "LOCAL") {
                $("#branchListTitle").html("本地分支")
                $.cookie('branchTitle', 'LOCAL');
            }
            //alert(result.data)
            $("#branchTable").html("")

            $("#branchTable").append(
                "<tr class='active'>" +
                    "<td><span class='glyphicon glyphicon-th-list' aria-hidden='true'></span>分支名</td>" +
                    "<td><span class='glyphicon glyphicon-time' aria-hidden='true'></span>上次修改时间</td> " +
                    "<td><span class='glyphicon glyphicon-user' aria-hidden='true'></span>提交者</td> " +
                    "<td><span class='glyphicon glyphicon-tags' aria-hidden='true'></span>备注</td> " +
                    "<td><span class='glyphicon glyphicon-cog' aria-hidden='true'></span>操作</td> " +
                    "<td><span class='glyphicon glyphicon-cloud' aria-hidden='true'></span>打开链接</td>" +
                "</tr>"
            )

            if (result.data.length <= 0) {
                $("#branchTable").append("<a3>空空如也~</a3>");
                return
            }

            $.each(JSON.parse(result.data), function (index, value) {
                $("#branchTable").append(
                    "<tr>" +
                    "<td>" + value.name + "</td>" +
                    "<td>2018/02/03</td>" +
                    "<td>Sober</td>" +
                    "<td>优化PRD, 调整布局</td>" +
                    "<td>" +
                    "<div class='btn-group' role='group' aria-label='...'>" +
                    "<button type='button' class='btn btn-default'>提交</button>" +
                    "<button type='button' class='btn btn-default' onclick= delBranch('"+value.name+"')>删除</button>" +
                    "</div>" +
                    "<div class='btn-group' role='group' aria-label='...'>" +
                    "<button type='button' class='btn btn-default'>拉取更新</button>" +
                    "<button type='button' class='btn btn-default' disabled>撤销本地更改</button>" +
                    "</div>" +
                    "</td>" +
                    "<td>" +
                    "<u><a href=“#”>在线地址</a></u>&nbsp;" +
                    "<u><a href=“#”>在Finder中打开</a></u>" +
                    "</td>" +
                    "</tr>"
                );

            })


        } else {
            // do something
            alert(result.msg)
        }
    })
}

/**
 *  删除分支
 */
function delBranch(name) {
    $.post("/api/delBranch", {branchName : name}, function (result) {
        if (result.result) {
            alert("分支 : " + name + "删除成功!")

            var branchTable = $("#branchTable").text()
            if (branchTable == "全部分支") {
                getBranchList("ALL")
            }else if(branchTable == "本地分支") {
                getBranchList("LOCAL")
            }
        }else {
            alert("请求失败 请重试!")
        }
    })
}