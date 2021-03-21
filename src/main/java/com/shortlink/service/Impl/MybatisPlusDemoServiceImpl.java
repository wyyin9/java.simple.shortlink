package com.shortlink.service.Impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.shortlink.mapper.ShortlinkRecordMapper;
import com.shortlink.model.entity.ShortlinkRecord;
import com.shortlink.service.MyBatisPlusDemoService;
import org.springframework.stereotype.Service;

@Service
public class MybatisPlusDemoServiceImpl extends ServiceImpl<ShortlinkRecordMapper, ShortlinkRecord> implements MyBatisPlusDemoService {


}
