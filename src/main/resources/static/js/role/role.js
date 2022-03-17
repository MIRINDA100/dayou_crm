layui.use(['table','layer'],function(){
       var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

       /**
        * 加载数据表格
        */
       var tableIns = table.render({
              id:'roleTable'
              // 容器元素的ID属性值
              ,elem: '#roleList'
              // 容器的高度 full-差值
              ,height: 'full-125'
              // 单元格最小的宽度
              ,cellMinWidth:95
              // 访问数据的URL（后台的数据接口）
              ,url: ctx + '/role/list'
              // 开启分页
              ,page: true
              // 默认每页显示的数量
              ,limit:10
              // 每页页数的可选项
              ,limits:[10,20,30,40,50]
              // 开启头部工具栏
              ,toolbar:'#toolbarDemo'
              // 表头
              ,cols: [[
                     // field：要求field属性值与返回的数据中对应的属性字段名一致
                     // title：设置列的标题
                     // sort：是否允许排序（默认：false）
                     // fixed：固定列
                     {type:'checkbox', fixed:'center'}
                     ,{field: 'id', title: '编号',  sort: true, fixed: 'left'}
                     ,{field: 'roleName', title: '角色名称', align:'center'}
                     ,{field: 'roleRemark', title: '角色备注', align:'center'}
                     ,{field: 'createDate', title: '创建时间', align:'center'}
                     ,{field: 'updateDate', title: '修改时间', align:'center'}
                     ,{title:'操作',templet:'#roleListBar', fixed: 'right', align:'center', minWidth:150}
              ]]
       });



       //头工具栏事件
       table.on('toolbar(roles)', function(obj){
              var checkStatus = table.checkStatus(obj.config.id);
              switch(obj.event){
                     case "add":
                            openAddOrUpdateRoleDialog();
                            break;
                     case "grant":
                            openAddGrantDailog(checkStatus.data);
                            break;
              };
       });

       function openAddGrantDailog(datas){
              if(datas.length==0){
                     layer.msg("请选择待授权角色记录!", {icon: 5});
                     return;
              }
              if(datas.length>1){
                     layer.msg("暂不支持批量角色授权!", {icon: 5});
                     return;
              }
              var url  =  ctx+"/role/toAddGrantPage?roleId="+datas[0].id;
              var title="角色管理-角色授权";
              layui.layer.open({
                     title : title,
                     type : 2,
                     area:["600px","600px"],
                     maxmin:true,
                     content : url
              });
       }


       /**
        * 行监听
        */
       table.on("tool(roles)", function(obj){
              var layEvent = obj.event;
              if(layEvent === "edit") {
                     openAddOrUpdateRoleDialog(obj.data.id);
              }
              else if(layEvent === "del") {
                     layer.confirm('确定删除当前角色？', {icon: 3, title: "角色管理"}, function (index) {
                            $.post(ctx+"/role/delete",{roleId:obj.data.id},function (data) {
                                   if(data.code==200){
                                          layer.msg("操作成功！");
                                          tableIns.reload();
                                   }else{
                                          layer.msg(data.msg, {icon: 5});
                                   }
                            });
                     })
              }
       });
       // 打开添加页面
       function openAddOrUpdateRoleDialog(uid){
              var url  =  ctx+"/role/addOrUpdateRolePage";
              var title="角色管理-角色添加";
              if(uid){
                     url = url+"?id="+uid;
                     title="角色管理-角色更新";
              }
              layui.layer.open({
                     title : title,
                     type : 2,
                     area:["600px","280px"],
                     maxmin:true,
                     content : url
              });
       }








});
