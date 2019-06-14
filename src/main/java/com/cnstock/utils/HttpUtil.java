package com.cnstock.utils;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLHandshakeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.*;

/**
 * Created by Administrator on 2018/12/28.
 */
public class HttpUtil {
    private  static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    public static String[] httpPost(String urlAddr, String jsonInputString,HashMap headMap){
        String[] str = new String[2];
        String returnString = "";
        HttpURLConnection conn = null;
        int code =0;
        try {
            URL url = new URL(urlAddr);
            conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Content-Length",String.valueOf(jsonInputString.length()));
            Random random = new Random();
            List<String> s = ReadUtil.ReadUserAgent();
            if(s.size()>0){
                int i = random.nextInt(s.size());
                conn.setRequestProperty("User-agent",s.get(i));
            }

            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            if(headMap!=null){
                Iterator iterator = headMap.keySet().iterator();
                while(iterator.hasNext()) {
                    String key = (String)iterator.next();
                    conn.setRequestProperty(key, (String)headMap.get(key));
                }
            }
            conn.setDoInput(true);
            conn.connect();

            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(jsonInputString);
            out.flush();
            out.close();

            code = conn.getResponseCode();
            if (code == 200||code == 202) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                returnString = sb.toString();
            }
        }catch (SocketTimeoutException e){
            str[1]="timeout";
            return str;
        }catch (Exception ex) {
            str[1]="timeout";
        } finally {
            conn.disconnect();
        }
        str[0]=code+"";
        str[1]=returnString;
        return str;
    }

    public static String doPost(String url, Map<String, Object> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key).toString()));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,Consts.UTF_8);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }

    public static String[] doGet(String url, Map<String, String> param,String format) {

        String[] str = new String[2];
        int code = 0;
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            Random random = new Random();
            List<String> s = ReadUtil.ReadUserAgent();
            if (s.size() > 0) {
                int i = random.nextInt(s.size());
                httpGet.setHeader("User-agent", s.get(i));
            }
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(8000).setConnectTimeout(8000).build();//设置请求和传输超时时间

            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                resultString = EntityUtils.toString(response.getEntity(), format);
                str[0] = "200";
                str[1] = resultString;
            } else {
                str[0] = code + "";
            }
        } catch (ConnectTimeoutException e){
            str[0]="408";
            str[1]="timeout";
        } catch (SocketTimeoutException e){
            str[0]="408";
            str[1]="timeout";
        } catch (SSLHandshakeException e){
            logger.info(url + "\tSSL异常");
            str[0]="500";
            str[1]="SSL异常";
        } catch (UnknownHostException e){
            logger.info(url + "\t网站地址无效");
            str[0]="0";
            str[1]="网站地址无效";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public static String[] doPost(String url, Map<String, String> param,String format) {
        String[] str = new String[2];
        int code =0;
        RequestConfig requestConfig = RequestConfig.custom().
                setSocketTimeout(8000).setConnectTimeout(8000).setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            code = response.getStatusLine().getStatusCode();
            if (code== 200) {
                resultString = EntityUtils.toString(response.getEntity(), format);
                str[0]="200";
                str[1]=resultString;
            } else {
                str[0]=code+"";
            }
        } catch (ConnectTimeoutException e){
            str[0]="408";
            str[1]="timeout";
        } catch (SocketTimeoutException e){
            str[0]="408";
            str[1]="timeout";
        } catch (SSLHandshakeException e){
            logger.info(url + "\tSSL异常");
            str[0]="500";
            str[1]="SSL异常";
        } catch (UnknownHostException e){
            logger.info(url + "\t网站地址无效");
            str[0]="0";
            str[1]="网站地址无效";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response!=null)response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public static String post(String url, Map<String, Object> param) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key).toString()));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(),"utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response!=null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }
    public static String postParams(String  url,String param) {
        // 获取连接客户端工具
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String entityStr = null;
        CloseableHttpResponse response = null;

        try {

            // 创建POST请求对象
            HttpPost httpPost = new HttpPost(url);

        /*
         * 添加请求参数
         */
            // 创建请求参数
            List<NameValuePair> list = new LinkedList<>();
            BasicNameValuePair param1 = new BasicNameValuePair("param", param);
            list.add(param1);

            // 使用URL实体转换工具
            UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
            httpPost.setEntity(entityParam);

        /*
         * 添加请求头信息
         */
            // 浏览器表示
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
            // 传输的类型
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.addHeader("Content-Type", "application/json");
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(200000).setConnectTimeout(200000).build();//设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpPost);
            // 获得响应的实体对象
            HttpEntity entity = response.getEntity();
            // 使用Apache提供的工具类进行转换成字符串
            entityStr = EntityUtils.toString(entity, "UTF-8");

            // System.out.println(Arrays.toString(response.getAllHeaders()));

        } catch (SocketTimeoutException e){
            logger.info("{}  连接超时",url);
        } catch(SocketException e){
            System.err.println("Socket异常");
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            System.err.println("Http协议出现问题");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("解析错误");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO异常");
            e.printStackTrace();
        } finally {
            // 释放连接
            if (null != response) {
                try {
                    response.close();
                    httpClient.close();
                } catch (IOException e) {
                    System.err.println("释放连接出错");
                    e.printStackTrace();
                }
            }
        }

        // 打印响应内容
        return entityStr;
    }


    public static String parse(String url){
        String str ="";
        try {
            Document document = Jsoup.parse(new URL(url).openStream(), "GBK", url);
            str = document.body().toString();
        } catch (IOException e) {
            return "";
        }
        return str;
    }


    public static void main(String[] args) throws Exception {
        Map map = new HashMap();
        map.put("url","http://www.miit.gov.cn/gdnps/wjfbindex.jsp");
        String[] strings = HttpUtil.doPost("http://172.20.3.143:8999/parse", map, "utf-8");
        System.out.println(strings[0]);
        System.out.println(strings[1]);
    }
}
