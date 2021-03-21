package com.shortlink.task;

import com.shortlink.service.ShortlinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ShortlinkRecordSyncTask {

    /**
     * 短链服务
     */
    @Autowired
    private ShortlinkService shortlinkService;

    /**
     * 程序启动后运行了几次
     */
    private static int syncCount = -1;

    /**
     * 执行同步
     */
    @Scheduled(fixedRate = 10000) // 间隔时间10000毫秒
    public void Handle() {
        syncCount++;
        // 首次启动先不执行
        if (syncCount <= 0) {
            return;
        }

        System.out.println("启动同步" + new Date());
        // 开始同步
        shortlinkService.syncBuildRecord();
    }
}
