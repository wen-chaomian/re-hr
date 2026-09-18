package com.hrsystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("oplog")
public class Oplog {
    private Integer id;

    @TableField(value = "addDate")
    private Date addDate;

    @TableField(value = "operate")
    private String operate;

    @TableField(value = "hrid")
    private Integer hrid;

    // ✅新增幂等msgId，数据库字段 msg_id
    @TableField(value = "msg_id")
    private String msgId;
}
