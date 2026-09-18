package com.hrmessage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hrmessage.entity.Oplog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OplogMapper extends BaseMapper<Oplog> {
}
