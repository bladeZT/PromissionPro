
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%@include file="/static/common/common.jsp"%>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/role.js"></script>
    <style>
        #dialog #myform .panel-header{
            height: 25px;
        }
        #dialog #myform .panel-title{
            margin-top: -5px;
            color: black;
        }
    </style>
</head>
<body>
    <%--表格上部 工具栏--%>
    <div id="toolbar">
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" id="add">添加</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" id="edit">编辑</a>
        <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" id="remove">删除</a>
    </div>
    <%--数据表格--%>
    <div id="role_dg"></div>
    <%--添加角色的对话框--%>
    <div id="dialog">
        <form id="myform">
            <table align="center" style="border-spacing: 20px 30px">
                <input type="hidden" name="rid">
                <tr align="center">
                    <td>角色编号: <input type="text" name="rnum" class="easyui-validatebox" ></td>
                    <td>角色名称: <input type="text" name="rname" class="easyui-validatebox" ></td>
                </tr>
                <tr>
                    <td><div id="role_data1"></div></td>
                    <td><div id="role_data2"></div></td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>
