package com.cnstock.service.impl;

import com.cnstock.entity.TbJob;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/5.
 */
@Service
public class HeartServiceImpl {
    private Logger logger = Logger.getLogger(HeartServiceImpl.class);
    private long lastTime = 0;
    @Autowired
    private TestServiceImpl testService;

    public void heart() {
        if (testService.jobWorkers.size() == 0 && testService.workers.size() == 0) {
            logger.info("=====================初始化任务=====================");
            testService.getTask(new ArrayList<TbJob>());
            lastTime = System.currentTimeMillis();
        } else {
            if (System.currentTimeMillis() - lastTime > 180000) {
                logger.info("====================开始发送心跳=====================");
                lastTime = System.currentTimeMillis();
                List<TbJob> tbJobs = new ArrayList<>();
                if (testService.workers.size() > 0) {
                    for (int i = 0; i < testService.workers.size(); i++) {
                        TbJob job = testService.workers.get(i);
                        tbJobs.add(job);
                    }
                }
                testService.getTask(tbJobs);
                logger.info("=====================发送心跳结束=====================");
            }
        }
    }


    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }
}
