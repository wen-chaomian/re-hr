package com.hrmessage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hrmessage.entity.Oplog;
import com.hrmessage.mapper.OplogMapper;
import com.hrmessage.service.OplogService;
import org.springframework.stereotype.Service;

@Service
public class OplogServiceImpl extends ServiceImpl<OplogMapper, Oplog> implements OplogService {
}
