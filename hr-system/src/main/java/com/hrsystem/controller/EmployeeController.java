package com.hrsystem.controller;

import com.hrsystem.clients.OplogFeignClient;
import com.hrsystem.entity.Employee;
import com.hrsystem.entity.Oplog;
import com.hrsystem.entity.RespBean;
import com.hrsystem.entity.RespPageBean;
import com.hrsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 员工控制器
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    /**
     * 分页查询员工
     * GET /employee
     */
    @GetMapping
    public RespPageBean getEmployee(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    Employee employee,
                                    Date[] beginDateScope) {
        return employeeService.getEmployeeByPage(page, size, employee, beginDateScope);
    }


    /**
     * 新增员工【重点，会发送RocketMQ消息】
     * POST /employee
     * 请求体：JSON
     */
    @PostMapping
    public String addEmp(@RequestBody Employee employee) {
        int result = employeeService.addEmp(employee);
        if(result == 1){
            return "success";
        }else {
            return "fail";
        }
    }

    /**
     * 获取最大工号
     */
    @GetMapping("/maxWorkId")
    public Integer maxWorkID() {
        return employeeService.maxWorkID();
    }

    /**
     * 删除员工
     */
    //修改员工
    @PutMapping("/update")
    public RespBean updateEmp(@RequestBody Employee employee){
        int res = employeeService.updateEmp(employee);
        return res>0 ? RespBean.ok("修改成功") : RespBean.error("修改失败");
    }

    //删除员工
    @DeleteMapping("/{id}")
    public RespBean deleteEmp(@PathVariable Integer id){
        int res = employeeService.deleteEmpByEid(id);
        return res>0 ? RespBean.ok("删除成功") : RespBean.error("删除失败");
    }


    /**
     * 根据id查询员工
     */
    @GetMapping("/{id}")
    public Employee getEmpById(@PathVariable Integer id) {
        return employeeService.getEmployeeById(id);
    }

    @Autowired
    private OplogFeignClient oplogFeignClient;

    @GetMapping("/test/log")
    public List<Oplog> testFeign(){
        //远程请求hr‑message服务，查询oplog表数据
        return oplogFeignClient.getAllLog();
    }

}
