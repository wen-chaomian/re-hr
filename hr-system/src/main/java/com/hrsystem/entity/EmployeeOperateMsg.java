package com.hrsystem.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工操作消息实体，通过RocketMQ传递
 */
@Data
public class EmployeeOperateMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工id（被操作对象ID）
     */
    private Integer empId;

    /**
     * 操作管理员id，执行操作的登录用户hr.id
     */
    private Integer hrid;

    /**
     * 操作类型：ADD新增 / UPDATE修改 / DELETE删除
     */
    private String operateType;

    private String msgId;
    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 操作描述
     */
    private String operateDesc;


}
