package com.cnstock.service.impl;

import com.cnstock.entity.TbJob;
import com.cnstock.service.WorkerService;
import com.cnstock.utils.HttpUtil;
import com.cnstock.utils.ReadUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Administrator on 2018/12/27.
 */
@Service
public class WorkerServiceImpl implements WorkerService{
    private Logger logger = Logger.getLogger(WorkerServiceImpl.class);
    private int jobLength = 200;
    private BlockingQueue<TbJob> workers = new ArrayBlockingQueue<>(jobLength);
    private BlockingQueue<TbJob> jobWorkers = new ArrayBlockingQueue<>(jobLength);
    private long startTime= 0;
    @Value(value = "${com.cnstock.ThreadNum}")
    private int ThreadNum;
    @Override
    public void staticScanHtml() {
        if(workers.poll()==null){
            workers = ReadUtil.readFile(workers);
        }
        String encoding = System.getProperty("file.encoding");
        logger.info("linux coding-- :"+encoding);
        logger.info("jobWorkers size-- :"+jobWorkers.size());
        logger.info("BlockingQueueSize*** "+workers.size());
        logger.info("run ThreadNum*** "+ThreadNum);
        Runnables r = new Runnables();
        startTime = System.currentTimeMillis();
        for(int i=0;i<ThreadNum;i++){
            new Thread(r).start();
        }
    }






    class Runnables implements Runnable{

        @Override
        public void run() {
            scanHtml();
        }
    }

    public void scanHtml(){
        try {
            boolean isRun = true;
            while (isRun){
                TbJob worker=workers.poll();

                if(worker!=null){
                    //错误数大于等于5，则不执行
                    if(worker.getErrorCount()>=5){
                        jobWorkers.offer(worker);
                        continue;
                    }

                    //执行http请求，获取text
                    String[] text = HttpUtil.httpPost(worker.getJobUrl(),"",null);
                    //计算hash
                    if(text[1]!=null&&!text[1].equals("timeout")){
                        if(text[1].equals("timeout")){
                            int error = worker.getErrorCount();
                            error++;
                            worker.setErrorCount(error);
                            logger.info("timeout***"+worker.getJobUrl()+"---errorcount---"+worker.getErrorCount());
                            workers.offer(worker);
                            continue;
                        }
                        int hashCode = text[1].hashCode();
                        //判断是否为第一次
                        if(Integer.parseInt(worker.getHashCode())==0){
                            logger.info(worker.getJobUrl()+"---init--"+hashCode);
                            worker.setHashCode(hashCode+"");
                            workers.offer(worker);
                        }else{
                            logger.info(worker.getJobUrl()+"---hashcode--"+hashCode);
                            if(Integer.parseInt(worker.getHashCode())!=hashCode){
                                //hash不同,则判断返回的code
                                switch (text[0]){
                                    case "200":
                                        logger.info(worker.getJobUrl()+"++hashNot+++"+hashCode);
                                        //解析html
                                        String htmlResult = "";//HtmlUtil.ScanHtmlA(text[1],worker);
                                        logger.info("htmlResult----"+htmlResult);
                                        //写文件
                                        worker.setContent(htmlResult);
                                        ReadUtil.createFile(worker);
                                        worker.setHashCode(hashCode+"");
                                        workers.offer(worker);
                                        //发送至cc
                                        break;
                                    case "202":
                                        break;
                                    case "404":
                                        //记录错误标记位
                                        int error = worker.getErrorCount();
                                        error++;
                                        worker.setErrorCount(error);
                                        logger.info(worker.getJobUrl()+"---errorcount---"+worker.getErrorCount());
                                        workers.offer(worker);
                                        break;
                                }
                                workers.offer(worker);
                            }
                        }
                    }else{
                        //记录错误标记位
                        int error = worker.getErrorCount();
                        error++;
                        worker.setErrorCount(error);
                        logger.info(worker.getJobUrl()+"--ResponseCode--"+text[0]+"---errorcount---"+worker.getErrorCount());
                        workers.offer(worker);
                    }

                }else{
                    long endTime = System.currentTimeMillis();
                    long runTime =endTime - startTime;
                    logger.info("job run time "+runTime);
                    logger.info("sleep time "+(60000-runTime));
                    logger.info("jobWorkers size"+jobWorkers.size());
                    if(runTime<=60000){
                        Thread.sleep((60000-runTime));
                    }
                    if(jobWorkers.size()==jobLength){
                        logger.info("1111111111111");
                        workers = new ArrayBlockingQueue<>(jobLength);
                        workers = jobWorkers;
                        jobWorkers = new ArrayBlockingQueue<>(jobLength);
                        startTime = System.currentTimeMillis();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
