$(function () {
    /*员式数据列表*/
    $("#dg").datagrid({
        url:"/employeeList",
        columns:[[
            {field:'username',title:'姓名',width:100,align:'center'},
            {field:'inputtime',title:'入职时间',width:100,align:'center'},
            {field:'tel',title:'电话',width:100,align:'center'},
            {field:'email',title:'邮箱',width:100,align:'center'},
            {field:'department',title:'部门',width:100,align:'center',formatter: function(value,row,index){
                if (value){
                    return value.name;
                }
            }},
            {field:'state',title:'状态',width:100,align:'center',formatter: function(value,row,index){
                    //value代表state这个的值，row，代表整个这一行所有的字段的集合
                    if(value){
                        return "在职";
                    }else {
                        return "<font style='color: red'>离职</font>"
                    }
                }},
            {field:'admin',title:'管理员',width:100,align:'center',formatter: function(value,row,index){
                    if(row.admin){
                        return "是";
                    }else {
                        return "否"
                    }
                }},
        ]],
        //填满区域
        fit:true,
        //左右可以拉伸
        fitColumns:true,
        //显示行号
        rownumbers:true,
        //分页工具
        pagination:true,
        //增加上部工具栏
        toolbar:"#tb",
        //只允许选中一行
        singleSelect:true,
        //设置斑马线显示效果
        striped:true,
        //设置点击一行的时候触发的事件
        onClickRow:function (rowIndex,rowData) {
            //判断当前是否是离职状态
            if(rowData.state){
                //没有离职，启用离职按钮
                $("#delete").linkbutton("enable");
            }else {
                //已经离职，禁用离职按钮
                //但实际上，并没有禁用掉，这是easyui自己的bug，所以我们自己写了base.js,重写了这个方法，实现了真正的禁用按钮
                $("#delete").linkbutton("disable");
            }
        }
    });
    /*对话框*/
    $("#dialog").dialog({
        width:350,
        height:400,
        //刚加载页面的时候，不显示这个对话框
        closed:true,
        buttons:[{
            text:'保存',
            handler:function(){

                //判断当前是编辑 还是添加 ---根据id有无
                //属性选择器
                var id = $("[name='id']").val();
                var url;
                if(id){
                    url = "updateEmployee"
                }else {
                    url = "saveEmployee";
                }

                /*提交表单*/
                $("#employeeForm").form("submit",{
                    url:url,
                    onSubmit:function(param){
                        //获取选中的角色
                        var values = $("#role").combobox("getValues");
                        for (var i = 0; i < values.length; i++) {
                            //取出每一个角色的id
                            var rid = values[i];
                            //封装到集合里面
                            param["roles["+i+"].rid"] = rid;
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
                            $("#dg").datagrid("reload");
                        } else {
                            $.messager.alert("温馨提示",data.msg);
                        }
                    }

                });
            }
        },{
            text:'关闭',
            handler:function(){
                $("#dialog").dialog("close");
            }
        }]
    });
    /*监听添加按钮点击*/
    $("#add").click(function () {
        $("#dialog").dialog("setTitle","添加员工");
        $("#password").show();
        //添加的时候，密码是必填项
        $("[name='password']").validatebox({required:true});
        //再重新点击添加的时候，应该清除上次写在对话框中from表单的数据
        $("#employeeForm").form("clear");
        $("#dialog").dialog("open");
    });
    /*监听编辑按钮点击*/
    $("#edit").click(function () {
        //获取当前选中的行
        var rowData = $("#dg").datagrid("getSelected");
        console.log(rowData);
        if(!rowData){
            $.messager.alert("提示","选择一行数据进行编辑")
            return;
        }
        //因为编辑的时候，没有编辑密码，若密码为空，则不能提交，因为在表单里规定了密码是必需的，所以要取消密码验证
        $("[name='password']").validatebox({required:false});
        $("#password").hide();
        //弹出对话框
        $("#dialog").dialog("setTitle","编辑员工");
        $("#dialog").dialog("open");
        $("#employeeForm").form("clear");
        //rowData中没有department.id这个键，而表单中有department.id这个name，故需要增加才能一一对应
        //要记得加 ""，在[]中,还有这里rowData["department"].id;，用id不用name，是因为在下拉框列表中value是id，text是name,但是我这里改成了name，也是正确的，i dont know why
        //又因为这个["department"]可能 没有给，如果为Null，直接赋值就会报错
        if(rowData["department"]){
            rowData["department.id"] = rowData["department"].id;
        }
        //回显管理员 把布尔型的数据变成字符串，这样就可以在下拉框中与valueField 对应
        //同样加if，也是为了防止没有给admin设置true or false 而所产生的错误
        if(rowData["admin"] == true || rowData["admin"] == false){
            rowData["admin"] = rowData["admin"] + "";
        }
        //回显角色
        //根据当前用户的id,查出对应的角色
        $.get("/getRoleByEid?id="+rowData.id,function (data) {
            console.log(data);
            //设置下拉列表数据回显
            $("#role").combobox("setValues",data);

        });

        console.log(rowData);
        //选中数据的回显,回显的时候与表单中的name的值与rowData中的键一一对应
        $("#employeeForm").form("load",rowData);

    });
    /*部门选择 下拉列表*/
    $("#department").combobox({
        width:150,
        panelHeight:'auto',
        editable:false,
        url:'departList',
        textField:'name',
        valueField:'id',
        onLoadSuccess:function () { /*数据加载完毕之后回调*/
            $("#department").each(function(i){
                var span = $(this).siblings("span")[i];
                console.log($(this).siblings("span"));
                console.log(span);
                var targetInput = $(span).find("input:first");
                console.log(targetInput);
                if(targetInput){
                    $(targetInput).attr("placeholder", $(this).attr("placeholder"));
                }
            });
        }
    });
    /*是否为管理员选择*/
    $("#state").combobox({
        width:150,
        panelHeight:'auto',
        textField:'label',
        valueField:'value',
        editable:false,
        data:[{
            label:'是',
            value:'true'
        },{
            label:'否',
            value:'false'
        }],
        onLoadSuccess:function () { /*数据加载完毕之后回调*/
            $("#state").each(function(i){
                //他下拉框的每一个选项，都是一个span
                var span = $(this).siblings("span")[i];
                var targetInput = $(span).find("input:first");
                if(targetInput){
                    $(targetInput).attr("placeholder", $(this).attr("placeholder"));
                }
            });
        }

    });
    /*选择 角色 的下拉列表*/
    $("#role").combobox({
        width:150,
        panelHeight:'auto',
        editable:false,
        url:'roleList',
        textField:'rname',
        valueField:'rid',
        //使这个下拉列表允许多选
        multiple:true,
        onLoadSuccess:function () { /*数据加载完毕之后回调*/
            $("#role").each(function(i){
                var span = $(this).siblings("span")[i];
                console.log($(this).siblings("span"));
                console.log(span);
                var targetInput = $(span).find("input:first");
                console.log(targetInput);
                if(targetInput){
                    $(targetInput).attr("placeholder", $(this).attr("placeholder"));
                }
            });
        }
    });
    /*设置 离职 按钮的点击*/
    $("#delete").click(function () {
        //获取当前选中的行
        var rowData = $("#dg").datagrid("getSelected");
        console.log(rowData);
        if(!rowData){
            $.messager.alert("提示","选择一行数据进行编辑")
            return;
        }
        //提醒用户，是否做离职的操作
        $.messager.confirm("确认","是否做离职操作",function (res) {
            //点确定，res是true，点取消，是false
            if(res){
                //做离职操作
                $.get("/updateState?id="+rowData.id,function (data) {
                    //让js识别，这是一个json的数据,但其实下面那个会报错
                    //因为form表单提交时候的回调函数的data需要自己解析为json，而get，ajax的data,已经为我们解析成了data,所以自己不需要再来一次
                    // data = $.parseJSON(data);
                    if (data.success){
                        //easyui的弹出框
                        $.messager.alert("温馨提示",data.msg);
                        /*重新加载数据表格*/
                        $("#dg").datagrid("reload");
                    } else {
                        $.messager.alert("温馨提示",data.msg);
                    }
                });
            }
        });
    });
    /*设置搜素按钮的点击*/
    $("#searchbtn").click(function () {
        //获取搜索框的内容
        var keyword = $("[name='keyword']").val();
        //重新加载列表，把参数keyword传过去
        $("#dg").datagrid("load",{keyword:keyword})
    });
    /*监听 刷新 按钮的点击*/
    $("#reload").click(function () {
        //情况搜索框的内容
        var keyword = $("[name='keyword']").val('');
        //重新加载内容
        $("#dg").datagrid("load",{});
    });
    /*监听 导出 按钮的点击*/
    $("#excelOut").click(function () {
        //弹出一个窗口
        //如果给的数据是流的形式，就会下载下来，保存到本地
        window.open("/downloadExcel");
    });
    /*导入excel的对话弹出框*/
    $("#excelUpload").dialog({
        width:260,
        height:180,
        title:"导入Excel",
        buttons:[{
            text:'保存',
            handler:function(){
                $("#uploadForm").form("submit",{
                    url:"uploadExcelFile",
                    success:function (data) {
                        //让js识别，这是一个json的数据
                        data = $.parseJSON(data);
                        if (data.success){
                            //easyui的弹出框
                            $.messager.alert("温馨提示",data.msg);
                            /*关闭对话框 */
                            $("#dialog").dialog("close");
                            /*重新加载数据表格*/
                            $("#dg").datagrid("reload");
                        } else {
                            $.messager.alert("温馨提示",data.msg);
                        }
                    }
                })
            }
        },{
            text:'关闭',
            handler:function(){
                $("#excelUpload").dialog("close");
            }
        }],
        closed:true
    });
    
    $("#excelImpot").click(function () {
        $("#excelUpload").dialog("open");
    });
    /*监听 导入 按钮的点击*/
    $("#excelIn").click(function () {
        $("#excelUpload").dialog("open");
    });
    /*监听 下载模板 的点击*/
    $("#downloadTml").click(function () {
        window.open("/downloadExcelTpl");
    });

});






