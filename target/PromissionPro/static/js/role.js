$(function () {
    /*角色数据表格*/
    $("#role_dg").datagrid({
        url:"/getRoles",
        columns:[[
            {field:'rnum',title:'角色编号',width:100,align:'center'},
            {field:'rname',title:'角色名称',width:100,align:'center'},

        ]],
        //填满区域
        fit:true,
        //左右可以拉伸
        fitColumns:true,
        //显示行号
        rownumbers:true,
        //分页工具
        pagination:true,
        //只允许选中一行
        singleSelect:true,
        //设置斑马线显示效果
        striped:true,
        //设置工具栏
        toolbar:"#toolbar"
    });
    /*添加/编辑角色的对话框*/
    $("#dialog").dialog({
        width:600,
        height:650,
        closed:true,
        buttons:[{
            text:'保存',
            handler:function(){
                //判断当前是添加还是编辑
                var rid = $("[name='rid']").val();
                var url;
                if(rid){
                    //编辑
                    url="updateRole"
                }else{
                    //添加
                    url="saveRole";
                }
                //提交表单
                $("#myform").form("submit",{
                    url:url,
                    //提交表单的时候有额外的参数(即不是通过name传的)，用param传给服务器
                    onSubmit:function(param){
                        //获取已选择的权限
                        var allRows = $("#role_data2").datagrid("getRows");
                        for (var i = 0; i < allRows.length; i++) {
                            //取出每一个权限
                            var row = allRows[i];
                            //封装到Role的permissions集合当中去
                            param["permissions[" + i + "].pid"] = row.pid;
                        }
                    },
                    success:function (data) {
                        //让js识别，这是一个json的数据
                        data = $.parseJSON(data);
                        if (data.success){
                            //easyui的弹出框
                            $.messager.alert("温馨提示",data.msg);
                            /*关闭对话框 */
                            $("#dialog").dialog("close");
                            /*重新加载数据表格*/
                            $("#role_dg").datagrid("reload");
                        } else {
                            $.messager.alert("温馨提示",data.msg);
                        }
                    }
                });
            }
        },{
            text:'关闭',
            handler:function(){

            }
        }]
    });
    /*权限列表*/
    $("#role_data1").datagrid({
        title:"所有权限",
        width:250,
        height:400,
        fitColumns:true,
        singleSelect:true,
        url:"permissionList",
        columns:[[
            {field:'pname',title:'权限名称',width:100,align:'center'}
        ]],
        //设置点击一行的时候触发的事件
        onClickRow:function (rowIndex,rowData) {
            //1.判断是否已经存在该权限
            //1.1取出所有的已选权限
            var allRows = $("#role_data2").datagrid("getRows");
            //1.2取出每一个进行判断
            for (var i = 0; i < allRows.length; i++) {
                var row = allRows[i];
                if(rowData.pid == row.pid){
                    //让已经存在的该权限，成为选中的状态
                    //获取已经成为选中状态当前角标
                    var index = $("#role_data2").datagrid("getRowIndex",row);
                    //将这个角标的行，设为选中状态
                    $("#role_data2").datagrid("selectRow",index);
                    return;
                }
            }
            //把当前选择的，添加到已选事件中
            $("#role_data2").datagrid("appendRow",rowData);

        }

    });
    /*选中的权限列表*/
    $("#role_data2").datagrid({
        title:"已选权限",
        width:250,
        height:400,
        fitColumns:true,
        singleSelect:true, 
        columns:[[
            {field:'pname',title:'权限名称',width:100,align:'center'}
        ]],
        onClickRow:function (rowIndex,rowData) {
            //删除当前选中的这一行
            $("#role_data2").datagrid("deleteRow",rowIndex);
        }

    });
    /*监听 添加按钮 的点击*/
    $("#add").click(function () {
        //清空表单中的数据
        $("#myform").form("clear");
        //清空已选权限
        $("#role_data2").datagrid("loadData",{rows:[]});
        //设置标题
        $("#dialog").dialog("setTitle","添加角色");
        $("#dialog").dialog("open");
    });
    /*监听 编辑按钮 的点击*/
    $("#edit").click(function () {
        //获取当前选中的行
        var rowData = $("#role_dg").datagrid("getSelected");
        if(!rowData){
            $.messager.alert("提示","选择一行数据进行编辑")
            return;
        }
        //选中数据的回显
        $("#myform").form("load",rowData);
        //加载当前角色下的权限
        var options = $("#role_data2").datagrid("options");
        //重新设置它的url
        options.url = "getPermissionByRid?rid="+rowData.rid;
        //重新加载数据z
        $("#role_data2").datagrid("load");
        //设置标题
        $("dialog").dialog("setTitle","编辑角色");
        //打开对话框
        $("#dialog").dialog("open");

    });
    /*监听 删除按钮 的点击*/
    $("#remove").click(function () {
        //获取当前选中的行
        var rowData = $("#role_dg").datagrid("getSelected");
        if(!rowData){
            $.messager.alert("提示","选择一行数据进行编辑")
            return;
        }
        $.messager.confirm("确认","是否进行删除操作",function (res) {
            if(res){
                $.get("deleteRole?rid="+rowData.rid,function (data) {
                    if (data.success){
                        //easyui的弹出框
                        $.messager.alert("温馨提示",data.msg);
                        /*重新加载数据表格*/
                        $("#role_dg").datagrid("reload");
                    } else {
                        $.messager.alert("温馨提示",data.msg);
                    }
                });
            }
        });

    });
});