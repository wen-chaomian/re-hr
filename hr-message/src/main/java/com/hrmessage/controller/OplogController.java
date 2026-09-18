package com.hrmessage.controller;

import com.hrmessage.entity.Oplog;
import com.hrmessage.service.OplogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/oplog")
public class OplogController {

    @Autowired
    private OplogService oplogService;

    //对外接口：查询全部操作日志，给hr‑system远程调用
    @GetMapping("/list")
    public List<Oplog> getAllLog(){
        return oplogService.list();
    }
}
