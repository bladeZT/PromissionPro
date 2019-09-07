package com.blade.web;

import com.blade.domain.AjaxRes;
import com.blade.domain.Employee;
import com.blade.domain.PageListRes;
import com.blade.domain.QueryVo;
import com.blade.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
//为了实现从excel表中取得不同数据类型数据，手动导的包
import org.apache.poi.ss.usermodel.CellType.*;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class EmployeeController {
    //注入业务层
    @Autowired
    private EmployeeService employeeService;


    @RequestMapping("/employee")
    //设置了权限标签，会调用shiro的授权方法，有这个权限才能访问
    @RequiresPermissions("employee:index")
    public String employee(){
        return "employee";
    }

    @RequestMapping("/employeeList")
    //加了这个，返回页面的时候，返回的就是json形式的数据
    @ResponseBody
    public PageListRes employeeList(QueryVo queryVo){
        //调用业务层，查询员工
        PageListRes pageListRes = employeeService.getEmployee(queryVo);
        return pageListRes;
    }

    /*接收员工数据的添加*/
    @RequestMapping("/saveEmployee")
    @ResponseBody
    @RequiresPermissions("employee:add")
    public AjaxRes saveEmployee(Employee employee){
        System.out.println("---------------------------");
        System.out.println("employee = " + employee);
        //自己的ajax返回的模板
        AjaxRes ajaxRes = new AjaxRes();
        try {
            //表单提交中，没有是否在职信息，默认是true，在职。
            employee.setState(true);
            //调用业务层，保存用户
            employeeService.saveEmployee(employee);
            ajaxRes.setMsg("保存成功");
            ajaxRes.setSuccess(true);
        } catch (Exception e) {
            ajaxRes.setMsg("保存失败");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }

    /*接受更新员工的请求*/
    @RequestMapping("/updateEmployee")
    @ResponseBody
    @RequiresPermissions("employee:edit")
    public AjaxRes updateEmployee(Employee employee){
        //自己的ajax返回的模板
        AjaxRes ajaxRes = new AjaxRes();
        try {
            //调用业务层更新员工
            employeeService.updateEmployee(employee);
            ajaxRes.setMsg("更新成功");
            ajaxRes.setSuccess(true);
        } catch (Exception e) {
            ajaxRes.setMsg("更新失败");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }
    /*接受离职操作的请求*/
    @RequestMapping("/updateState")
    @ResponseBody
    @RequiresPermissions("employee:delete")
    public AjaxRes updateState(Long id){
        //自己的ajax返回的模板
        AjaxRes ajaxRes = new AjaxRes();
        try {
            //调用业务层,设置员工的离职状态
            employeeService.updateState(id);
            ajaxRes.setMsg("更新成功");
            ajaxRes.setSuccess(true);
        } catch (Exception e) {
            System.out.println("e = " + e);
            ajaxRes.setMsg("更新失败");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }

    /*处理shiro当中的异常*/
    @ExceptionHandler(AuthorizationException.class)
    public void handleShiroException(HandlerMethod method, HttpServletResponse response) throws Exception {//method 这里是把发生异常的方法传了过来，拿到这个方法，就可以获取方法上贴的注解，根据有没有responsebody就知是不是json请求
        //跳转到一个界面，界面当中提示没有权限
        //如果是json,ajax形式访问的这个控制器，那我们就布恩那个直接在服务器中跳转，得让浏览器自己跳转
        //获取发生异常的方法的注解
        ResponseBody methodAnnotation = method.getMethodAnnotation(ResponseBody.class);
        if(methodAnnotation != null){
            //ajax
            AjaxRes ajaxRes = new AjaxRes();
            ajaxRes.setSuccess(false);
            ajaxRes.setMsg("你没有权限操作");
            String s = new ObjectMapper().writeValueAsString(ajaxRes);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(s);
        }else {
            response.sendRedirect("nopermission.jsp");
        }

    }

    /*接收 导出excel 的请求*/
    @RequestMapping("/downloadExcel")
    @ResponseBody
    public void downloadExcel(HttpServletResponse response){
        try {
            System.out.println("come----excel");
            //1.从数据库中取列表数据
            QueryVo queryVo = new QueryVo();
            queryVo.setPage(1);
            queryVo.setRows(10);
            PageListRes plr = employeeService.getEmployee(queryVo);
            List<Employee> employees = (List<Employee>)plr.getRows();
            //2.创建excel，写到excel中
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("员工数据");
            //创建一行，角标从0开始,0是头部
            HSSFRow row = sheet.createRow(0);
            //设置行的每一列的数据
            row.createCell(0).setCellValue("编号");
            row.createCell(1).setCellValue("用户名");
            row.createCell(2).setCellValue("入职日期");
            row.createCell(3).setCellValue("电话");
            row.createCell(4).setCellValue("邮件");

            //取出每一个员工来去设置数据
            HSSFRow employeeRow = null;
            for (int i = 0; i < employees.size(); i++) {
                Employee employee = employees.get(i);
                employeeRow = sheet.createRow(i + 1);
                employeeRow.createCell(0).setCellValue(employee.getId());
                employeeRow.createCell(1).setCellValue(employee.getUsername());
                if(employee.getInputtime() != null){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String format = sdf.format(employee.getInputtime());
                    employeeRow.createCell(2).setCellValue(format);
                }else {
                    employeeRow.createCell(2).setCellValue("");
                }
                employeeRow.createCell(3).setCellValue(employee.getTel());
                employeeRow.createCell(4).setCellValue(employee.getEmail());
            }
            //3.响应给浏览器
            String fileName = new String("员工数据.xls".getBytes("utf-8"), "iso8859-1");
            //3.2设置响应头,告诉浏览器以附件的形式下载
            response.setHeader("content-Disposition","attachment;fileName=" + fileName);
            //把excel写入到response输出流中
            wb.write(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*下载excel的模板*/
    @RequestMapping("/downloadExcelTpl")
    @ResponseBody
    public void downloadExcelTpl(HttpServletRequest request, HttpServletResponse response){
        FileInputStream is = null;
        try {
            //设置响应给浏览器的方式
            String fileName = new String("EmployeeTpl.xls".getBytes("utf-8"), "iso8859-1");
            response.setHeader("content-Disposition","attachment;fileName=" + fileName);

            //获取文件路径
            String realPath = request.getSession().getServletContext().getRealPath("static/EmployeeTpl.xls");
            System.out.println("realPath = " + realPath);
            is = new FileInputStream(realPath);
            //将is这个流，放到response的输出流中
            IOUtils.copy(is,response.getOutputStream());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*接收 上传的excel 的请求*/
    //要记得在mvc中，配置文件上传的解析器
    @RequestMapping("/uploadExcelFile")
    @ResponseBody
    public AjaxRes uploadExcelFile(MultipartFile excel){
        AjaxRes ajaxRes = new AjaxRes();
        try {
            HSSFWorkbook wb = new HSSFWorkbook(excel.getInputStream());
            HSSFSheet sheet = wb.getSheetAt(0);
            //获取最大的行号
            int lastRowNum = sheet.getLastRowNum();
            HSSFRow employeeRow = null;
            for (int i = 1; i <= lastRowNum; i++) {
                employeeRow = sheet.getRow(i);
                Employee employee = new Employee();
                getCellValue(employeeRow.getCell(0));
                employee.setUsername((String) getCellValue(employeeRow.getCell(1)));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String aa = getCellValue(employeeRow.getCell(2)).getClass().getSimpleName();
                System.out.println("aa = " + aa);
                employee.setInputtime((Date) getCellValue(employeeRow.getCell(2)));
                String telString = String.valueOf(getCellValue(employeeRow.getCell(3)));
                String[] array1 = telString.split("\\.");
                employee.setTel(array1[0]);
                String emailString = String.valueOf(getCellValue(employeeRow.getCell(4)));
                String[] array2 = emailString.split("\\.");
                employee.setEmail(array2[0]);
                employee.setPassword((String) getCellValue(employeeRow.getCell(5)));
                System.out.println("employee = " + employee);
                employeeService.saveEmployee(employee);
            }
            System.out.println("lastRowNum = " + lastRowNum);
            ajaxRes.setMsg("导入成功");
            ajaxRes.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            ajaxRes.setMsg("导入失败");
            ajaxRes.setSuccess(false);
        }
        return ajaxRes;
    }

    /*判断从excel表格中取出的数据是什么类型的*/
    private Object getCellValue(Cell cell){
        switch (cell.getCellType()) {
            case STRING:
                return cell.getRichStringCellValue().getString();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
        }
        return cell;
    }
}






















