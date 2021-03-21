package com.shortlink.service;

import com.shortlink.model.dto.BuildDto;
import com.shortlink.util.Result;

/**
 * 短链服务接口
 */
public interface ShortlinkService {

    /**
     * 构建短码
     * @return
     */
    Result buildShortCode(BuildDto request);

    /**
     * 通过短码获取原始连接信息
     * @return
     */
    Result getOriginalUrlByCode(String code);

    /**
     * 同步记录到数据库
     */
    void syncBuildRecord();

    /**
     * 重置
     * @return
     */
    String resetAll();
}
