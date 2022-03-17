layui.use(['form', 'layer','formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
        // 引入 formSelects 模块
        formSelects = layui.formSelects;


    /**
     * 添加或更新用户
     */
    form.on("submit(addOrUpdateUser)", function (data) {
        // 弹出loading层
        var index = top.layer.msg('数据提交中，请稍候', {icon: 16, time: false,
            shade: 0.8});
        var url = ctx + "/user/save";
        if($("input[name='id']").val()){
            url = ctx + "/user/update";
        }
        $.post(url, data.field, function (res) {
            if (res.code == 200) {
                setTimeout(function () {
                    // 关闭弹出层（返回值为index的弹出层）
                    top.layer.close(index);
                    top.layer.msg("操作成功！");
                    // 关闭所有ifream层
                    layer.closeAll("iframe");
                    // 刷新父页面
                    parent.location.reload();
                }, 500);
            } else {
                layer.msg(res.msg, {icon: 5});
            }
        });
        return false;
    });

    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    });


    /**
     *  加载角色下拉框
     *
     *  配置远程搜索, 请求头, 请求参数, 请求类型等
     *
     *  formSelects.config(ID, Options, isJson);
     *
     *  @param ID        xm-select的值
     *  @param Options   配置项
     *  @param isJson    是否传输json数据, true将添加请求头 Content-Type: application/json; charset=UTF-8
     */
    var userId = $("input[name='id']").val();
    formSelects.config('selectId',{
        type:"post",
        searchUrl:ctx+"/role/queryAllRoles?userId="+userId,
        keyName: 'roleName',  //自定义返回数据中name的key, 默认 name
        keyVal: 'id' //自定义返回数据中value的key, 默认 value
    },true);


});