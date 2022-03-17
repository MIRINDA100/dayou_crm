$(function () {
    // 加载树形结构
    loadModuleInfo();
});

// 定义树形结构对象
var zTreeObj;


/**
 * 加载资源树形数据
 */
function loadModuleInfo() {
    // zTree 的参数配置
    var setting = {
        data: {
            simpleData: {
                enable: true
            }
        },
        view:{
            showLine: false
            // showIcon: false
        },
        check: {
            enable: true,
            chkboxType: { "Y": "ps", "N": "ps" }
        },
        // 绑定函数
        callback: {
            // onCheck函数：当 checkbox/radio 被选中或取消选中时触发的函数
            onCheck: zTreeOnCheck
        }
    };
    // 通过ajax查询资源列表
    $.ajax({
        type:"post",
        url:ctx+"/module/queryAllModules",
        dataType:"json",
        // 查询所有的资源列表时，传递角色ID，查询当前角色对应的已经授权的资源
        data:{
            roleId:$("[name='roleId']").val()
        },
        success:function (data) {
            var zNodes =data;
            zTreeObj=$.fn.zTree.init($("#test1"), setting, zNodes);
        }
    })
}

function zTreeOnCheck(event, treeId, treeNode) {
    var nodes= zTreeObj.getCheckedNodes(true);
    // var roleId=$("#roleId").val();
    var roleId = $("[name='roleId']").val();
    var mids="mids=";
    for(var i=0;i<nodes.length;i++){
        if(i<nodes.length-1){
            mids=mids+nodes[i].id+"&mids=";
        }else{
            mids=mids+nodes[i].id;
        }
    }
    $.ajax({
        type:"post",
        url:ctx+"/role/addGrant",
        data:mids+"&roleId="+roleId,
        dataType:"json",
        success:function (data) {
            console.log(data);
        }
    })
}
