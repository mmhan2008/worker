package com.cnstock.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cnstock.entity.TbJob;
import com.cnstock.utils.AESUtils;
import com.cnstock.utils.HtmlUtil;
import com.cnstock.utils.HttpUtil;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2019/1/9.
 */
@Service
public class TestServiceImpl{
    private Logger logger = Logger.getLogger(TestServiceImpl.class);
    public int jobLength = 0;
    public List<TbJob> workers = new ArrayList<>();
    public BlockingQueue<TbJob> jobWorkers = new LinkedBlockingDeque<>();
    private long startTime= 0;
    private int workerNum = 0;
    private int totalNum = 0;
    @Autowired
    private HeartServiceImpl heartService;
    @Value(value = "${com.cnstock.ThreadNum}")
    private int ThreadNum;
    @Value(value = "${com.cnstock.WaitingTime}")
    private int WaitingTime;
    @Value(value = "${com.cnstock.token}")
    private String token;
    @Value(value = "${com.cnstock.sendPost}")
    private String url;
    @Value(value = "${com.cnstock.getTask}")
    private String taskUrl;
    public  LinkedHashMap<String , LinkedHashMap<String ,Boolean>> cache = new LinkedHashMap<>();
    @Value(value = "${com.cnstock.cachesize}")
    private  int cachesize;
    @Value("${pythonHost}")
    private String pyHost;
    @Value("${cookieHost}")
    private String cookieHost;

    public void testScanHtml(){
        heartService.heart();
        if (this.workers.size() > 0){
            for (TbJob wo : this.workers){
                this.jobWorkers.offer(wo);
            }
        }
        if (jobWorkers.size() > 0) {
            logger.info("工作任务数量 :" + jobWorkers.size());
            logger.info("运行的线程数：" + ThreadNum);
            this.workers.clear();
            startTime = System.currentTimeMillis();
            ExecutorService pool = Executors.newFixedThreadPool(ThreadNum);
            List<Callable<String>> list = new ArrayList<>();
            for (int i = 0; i < ThreadNum; i++) {
                list.add(new TestRunnables());
            }
            try {
                pool.invokeAll(list);
            } catch (InterruptedException e){
                logger.info(e);
            } finally {
                pool.shutdown();
            }
        }
    }

    class TestRunnables implements Callable<String>{
        @Override
        public String call() throws Exception {
            try {
                List<String> res = new ArrayList<>();
                while (true){
                    if (jobWorkers.size() == 0){
                        if (res.size()>0) {
                            //发送结果至CC
                            Map<String,Object> maps = new HashMap<>();
                            maps.put("message",res);
                            String param = JSON.toJSONString(maps);
                            HttpUtil.postParams(url,param);
                        }
                        break;
                    }
                    TbJob worker = jobWorkers.poll();
                    if( worker!=null ){
                        //错误数大于等于10，则不执行
                        if(worker.getErrorCount()!=null && worker.getErrorCount() >= 10){
                            workers.add(worker);
                            continue;
                        }
                        String[] text;
                        //执行http请求，获取text
                        Map map = new HashMap();
                        if(worker.getJobModel()!=null && worker.getJobModel().contains("COOKIE")){
                            if (worker.getJobModel().contains("PY")){
                                map.put("url",worker.getJobUrl());
                                text = HttpUtil.doPost(cookieHost,map,"utf-8");
                            } else {
                                text= HttpUtil.doPost(worker.getJobUrl(),null,worker.getJobModel().split("-")[0]);
                            }
                        }else{
                            if (worker.getJobModel()!=null&&worker.getJobModel().contains("PY")){
                                map.put("url",worker.getJobUrl());
                                map.put("type",worker.getJobModel());
                                text = HttpUtil.doPost(pyHost,map,"utf-8");
                            } else {
                                text= HttpUtil.doGet(worker.getJobUrl(),null,worker.getJobModel());
                            }
                        }
                        if("timeout".equals(text[1])){
                            int error = worker.getErrorCount() == null?0:worker.getErrorCount();
                            error++;
                            worker.setErrorCount(error);
                            workers.add(worker);
                            continue;
                        }
                        int hashCode = StringUtils.isEmpty(text[1]) ? 0 : text[1].hashCode();
                        //判断内容与上次是否相同
                        if(String.valueOf(hashCode).equals(worker.getHashCode())) {
                            worker.setHashCode(hashCode+"");
                            workers.add(worker);
                            continue;
                        }
                        Integer e = worker.getErrorCount()==null?0:worker.getErrorCount();
                        if ("200".equals(text[0])){
                            e--;
                            worker.setErrorCount(e);
                            worker.setHashCode(hashCode+"");
                            workers.add(worker);
                            //解析html
                            List<String> result = ScanHtmlA(text[1], worker);
                            if (result.size() > 0){
                                for (String s:result ) {
                                    res.add(s);
                                }
                            }
                        } else {
                            e++;
                            worker.setErrorCount(e);
                            worker.setErrorType(text[0] == null ? "0":text[0]);
                            workers.add(worker);
                        }
                    }
                }
                long endTime = System.currentTimeMillis();
                long runTime = endTime - startTime;
                logger.info("线程："+ Thread.currentThread().getName() + "运行时间（毫秒）" + runTime);
                if (runTime <= WaitingTime) {
                    Thread.sleep((WaitingTime - runTime));
                    logger.info("线程："+ Thread.currentThread().getName() + "休眠时间（毫秒）" + (WaitingTime-runTime));
                } else {
                    logger.info("线程："+ Thread.currentThread().getName() + "工作时间，超出预期");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public void getTask(List<TbJob> workers){
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
            String hostAddress = address.getHostAddress();
            Map<String,Object> maps = new HashMap<>();
            maps.put("list",JSON.toJSONString(workers));
            maps.put("workerId",hostAddress);
            maps.put("totalNum",totalNum);
            maps.put("workerNum",workerNum);
            String result =HttpUtil.doPost(taskUrl,maps);
            if(result != null){
                Map<String, Object> params = (Map<String, Object>) JSON.parse(result);
                totalNum = Integer.parseInt(
                        StringUtils.isEmpty(params.get("totalNum"))? "0":params.get("totalNum").toString());
                workerNum = Integer.parseInt(
                        StringUtils.isEmpty(params.get("workerNum"))?"0":params.get("workerNum").toString());
                String ll = StringUtils.isEmpty(params.get("list")) ? null : params.get("list").toString();
                List<JSONObject> list = new ArrayList<>();
                if (!StringUtils.isEmpty(ll)){
                    list = JSON.parseObject(ll,List.class);
                }
                if (!list.isEmpty() && list.size() > 0){
                    this.workers.clear();
                    for (JSONObject oJob : list){
                        TbJob job = JSON.parseObject(oJob.toJSONString(),TbJob.class);
                        this.workers.add(job);
                    }
                    logger.info("队列大小：" + list.size());
                    logger.info("******************************************************");
                    for (JSONObject o : list) {
                        TbJob job = JSON.parseObject(o.toJSONString(),TbJob.class);
                        logger.info(job.getJobName() + "\t" + job.getJobUrl());
                    }
                    logger.info("******************************************************");
                }
            } else {
                logger.info("********************未返回任何数据********************");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Scheduled(cron = "0 0/1 * * * *")
    public void taskJob(){
        logger.info("~~~~~~~~~~~~~start working~~~~~~~~~~~~~~~~");
        testScanHtml();
    }

    /**
     * 解析html
     * @param content  html内容
     */
    public List<String> ScanHtmlA(String content, TbJob job) throws UnsupportedEncodingException {
        List<String> result = new ArrayList<>();
        Document doc = Jsoup.parse(content);
        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            StringBuffer html = new StringBuffer();
            String linkText = link.attr("title");
            String linkHref = link.attr("href");
            if(linkHref.contains("javascript") || linkHref.contains("javaScript"))
                continue;

            if(!HtmlUtil.filtrationHtml(linkHref,job.getInclude1(),job.getInclude2(),job.getInclude3(),job.getIsInclude1(),job.getIsInclude2(),job.getIsInclude3()))
                continue;

            if(linkText==null||linkText.equals(""))
                linkText = link.text();
            String absUrl=HtmlUtil.getAbsoluteURL(job.getJobUrl(),linkHref);
            if(absUrl.equals("")){
                absUrl=linkHref;
            }
            String linkMd5 =AESUtils.encryption(absUrl);
            html.append(absUrl);
            html.append("###");
            if(linkText==null || linkText.equals("")){
                linkText="null";
            }else{
                linkText = HtmlUtil.delTagsFContent(linkText);
                linkText = HtmlUtil.filterStr(linkText);
            }
            html.append(linkText);
            html.append("###");
            if(StringUtils.isEmpty(job.getJobName()))
                html.append("null");
            else
                html.append(job.getJobName());
            result.add(html.toString());
        }
        return result;
    }
}
