package com.hrsystem.service;

import com.hrsystem.entity.Employee;
import com.hrsystem.entity.EmployeeOperateMsg;
import com.hrsystem.entity.RespPageBean;
import com.hrsystem.mapper.EmployeeMapper;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 * @时间 2019-10-29 7:44
 */
@Service
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    //注入RocketMQ生产者模板
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public final static Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    DecimalFormat decimalFormat = new DecimalFormat("##.00");

    public RespPageBean getEmployeeByPage(Integer page, Integer size, Employee employee, Date[] beginDateScope) {
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Employee> data = employeeMapper.getEmployeeByPage(page, size, employee, beginDateScope);
        Long total = employeeMapper.getTotal(employee, beginDateScope);
        RespPageBean bean = new RespPageBean();
        bean.setData(data);
        bean.setTotal(total);
        return bean;
    }

    public Integer addEmp(Employee employee) {
        Date beginContract = employee.getBeginContract();
        Date endContract = employee.getEndContract();
        double month = (Double.parseDouble(yearFormat.format(endContract)) - Double.parseDouble(yearFormat.format(beginContract))) * 12
                + (Double.parseDouble(monthFormat.format(endContract)) - Double.parseDouble(monthFormat.format(beginContract)));
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(month / 12)));
        int result = employeeMapper.insertSelective(employee);

        // 原项目RabbitMQ邮件发送逻辑全部移除，后续我们在这里自己接入RocketMQ，作为二次开发改造点
        if (result == 1) {
            // 新增员工发送RocketMQ消息
            EmployeeOperateMsg msg = new EmployeeOperateMsg();
            msg.setEmpId(employee.getId());
            msg.setHrid(1);  // ✅本地调试固定操作管理员id=1
            msg.setOperateType("ADD");
            msg.setOperateTime(new Date());
            msg.setOperateDesc("新增员工：" + employee.getName());
            msg.setMsgId(UUID.randomUUID().toString());
            //投递到topic：employee_operate_topic
            rocketMQTemplate.convertAndSend("employee_operate_topic", msg);
            logger.info("发送新增员工MQ消息成功,empId={},操作人hrid={}", employee.getId(), 1);
        }

        return result;
    }

    public Integer maxWorkID() {
        return employeeMapper.maxWorkID();
    }

    public Integer deleteEmpByEid(Integer id) {
        //删除之前先查询员工，拿到员工名称
        Employee employee = employeeMapper.selectByPrimaryKey(id);
        int result = employeeMapper.deleteByPrimaryKey(id);

        if (result == 1 && employee != null) {
            EmployeeOperateMsg msg = new EmployeeOperateMsg();
            msg.setEmpId(id);
            msg.setHrid(1);
            msg.setOperateType("DELETE");
            msg.setOperateTime(new Date());
            msg.setOperateDesc("删除员工：" + employee.getName());
            msg.setMsgId(UUID.randomUUID().toString());
            rocketMQTemplate.convertAndSend("employee_operate_topic", msg);
            logger.info("发送删除员工MQ消息成功,empId={},操作人hrid={}", id, 1);
        }
        return result;
    }

    public Integer updateEmp(Employee employee) {
        int result = employeeMapper.updateByPrimaryKeySelective(employee);
        if (result == 1) {
            EmployeeOperateMsg msg = new EmployeeOperateMsg();
            msg.setEmpId(employee.getId());
            msg.setHrid(1);
            msg.setOperateType("UPDATE");
            msg.setOperateTime(new Date());
            msg.setOperateDesc("修改员工：" + employee.getName());
            msg.setMsgId(UUID.randomUUID().toString());
            rocketMQTemplate.convertAndSend("employee_operate_topic", msg);
            logger.info("发送修改员工MQ消息成功,empId={},操作人hrid={}", employee.getId(), 1);
        }
        return result;
    }


    public Integer addEmps(List<Employee> list) {
        return employeeMapper.addEmps(list);
    }

    public Employee getEmployeeById(Integer empId) {
        return employeeMapper.getEmployeeById(empId);
    }
}
