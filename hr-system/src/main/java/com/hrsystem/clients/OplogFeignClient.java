package com.hrsystem.clients;

import com.hrsystem.entity.Oplog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

// value = 目标服务的spring.application.name，也就是hr‑message
// 原来：@FeignClient("hr-message")
@FeignClient(name = "hr-message")
public interface OplogFeignClient {
    @GetMapping("/oplog/list")
    List<Oplog> getAllLog();
}
