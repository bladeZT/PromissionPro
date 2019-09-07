$(function () {

    $("#menu_datagrid").datagrid({
        url:"/menuList",
        columns:[[
            {field:'text',title:'名称',width:1,align:'center'},
            {field:'url',title:'跳转地址',width:1,align:'center'},
            {field:'parent',title:'父菜单',width:1,align:'center',formatter:function(value,row,index){
                    return value?value.text:'';
                }
            }
        ]],
        fit:true,
        rownumbers:true,
        singleSelect:true,
        striped:true,
        pagination:true,
        fitColumns:true,
        toolbar:'#menu_toolbar'
    });

    /*
     * 初始化新增/编辑对话框
     */
    $("#menu_dialog").dialog({
        width:300,
        height:300,
        closed:true,
        buttons:'#menu_dialog_bt'
    });
    /*显示父菜单下拉框*/
    $("#parentMenu").combobox({
        width:150,
        panelHeight:'auto',
        editable:false,
        url:'parentList',
        textField:'text',
        valueField:'id',
        onLoadSuccess:function () { /*数据加载完毕之后回调*/
            $("#parentMenu").each(function(i){
                var span = $(this).siblings("span")[i];
                var targetInput = $(span).find("input:first");
                if(targetInput){
                    $(targetInput).attr("placeholder", $(this).attr("placeholder"));
                }
            });
        }
    });
    /*新增菜单*/
    $("#add").click(function () {
        $("#menu_form").form("clear");
        $("#menu_dialog").dialog("setTitle","添加菜单");
        $("#menu_dialog").dialog("open");
    });
    /*编辑菜单*/
    $("#edit").click(function () {
        $("#menu_form").form("clear");
        //获取当前选中的行
        var rowData = $("#menu_datagrid").datagrid("getSelected");
        console.log(rowData);
        if(!rowData){
            $.messager.alert("提示","选择一行数据进行编辑")
            return;
        }
        //父菜单回显
        if(rowData.parent){
            rowData["parent.id"] = rowData.parent.id;
        }else {
            $("#parentMenu").each(function(i){
                var span = $(this).siblings("span")[i];
                var targetInput = $(span).find("input:first");
                if(targetInput){
                    $(targetInput).attr("placeholder", $(this).attr("placeholder"));
                }
            });
        }
        $("#menu_dialog").dialog("setTitle","编辑菜单");
        $("#menu_dialog").dialog("open");
        $("#menu_form").form("load",rowData);
    });
    /*删除菜单*/
    $("#del").click(function () {
        //获取当前选中的行
        var rowData = $("#menu_datagrid").datagrid("getSelected");
        if(!rowData){
            $.messager.alert("提示","选择一行数据进行删除");
            return;
        }
        //提醒用户，是否做离职的操作
        $.messager.confirm("确认","是否做删除操作",function (res) {
            //点确定，res是true，点取消，是false
            if(res){
                //做离职操作
                $.get("/deleteMenu?id="+rowData.id,function (data) {
                    //让js识别，这是一个json的数据,但其实下面那个会报错
                    //因为form表单提交时候的回调函数的data需要自己解析为json，而get，ajax的data,已经为我们解析成了data,所以自己不需要再来一次
                    // data = $.parseJSON(data);
                    if (data.success){
                        //easyui的弹出框
                        $.messager.alert("温馨提示",data.msg);
                        //重新加载数据表格
                        $("#menu_datagrid").datagrid("reload");
                        //重新加载父菜单的下拉框数据
                        $("#parentMenu").combobox("reload");
                    } else {
                        $.messager.alert("温馨提示",data.msg);
                    }
                });
            }
        });
    });
    /*监听 对话框中保存 的点击*/
    $("#save").click(function () {

        //判断当前是保存还是编辑
        var id = $("[name='id']").val();
        var url;
        if(id){
            var patent_id = $("[name='parent.id']").val();
            if(id == patent_id){
                $.messager.alert("温馨提示","不能设置自己为父菜单");
                return;

            }
            url = "updateMenu"
        }else {
            url = "saveMenu";
        }
        /*提交表单*/
        $("#menu_form").form("submit",{
            url:url,
            success:function (data) {
                //让js识别，这是一个json的数据
                data = $.parseJSON(data);
                if (data.success){
                    //easyui的弹出框
                    $.messager.alert("温馨提示",data.msg);
                    //关闭对话框
                    $("#menu_dialog").dialog("close");
                    //重新加载父菜单的下拉框数据
                    $("#parentMenu").combobox("reload");
                    //重新加载数据表格
                    $("#menu_datagrid").datagrid("reload");
                } else {
                    $.messager.alert("温馨提示",data.msg);
                }
            }

        });
    });
    /*监听 对话框中取消 的点击*/
    $("#cancel").click(function () {
        $("#menu_dialog").dialog("close");
    });

});