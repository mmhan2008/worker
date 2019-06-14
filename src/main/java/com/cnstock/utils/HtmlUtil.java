package com.cnstock.utils;

import com.cnstock.entity.TbJob;
import com.cnstock.entity.Worker;
import com.cnstock.service.impl.TestServiceImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2019/1/7.
 */
public class HtmlUtil {

    private static Logger logger = LoggerFactory.getLogger(HtmlUtil.class);

    /**
     * 过滤html标签
     * @param content
     * @return
     * @throws Exception
     */
    public static String delTagsFContent(String content){
        return content.replaceAll("</?[a-zA-Z]+[^><]*>","");
    }
    /**
     * 过滤特殊字符
     * @param str
     * @return
     * @throws Exception
     */
    public static String filterStr(String str){
        try {
            String regEx = "[`~!@#$%^&*()+=|{}:;\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            str = Pattern.compile(regEx).matcher(str).replaceAll("").trim();
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 过滤链接
     * @param href  原链接
     * @return  返回新链接
     */
    public static boolean filtrationHtml(String href,TbJob tbJob){
        String include1 = tbJob.getInclude1()!=null?tbJob.getInclude1():null;
        String include2 = tbJob.getInclude2()!=null?tbJob.getInclude2():null;
        String include3 = tbJob.getInclude3()!=null?tbJob.getInclude3():null;
        String isInclude1 = tbJob.getIsInclude1()!=null?tbJob.getIsInclude1():null;
        String isInclude2 = tbJob.getIsInclude2()!=null?tbJob.getIsInclude2():null;
        String isInclude3 = tbJob.getIsInclude3()!=null?tbJob.getIsInclude3():null;
        if(href.contains(isInclude1)||href.contains(isInclude2)||href.contains(isInclude3)){
            return false;
        }else{
            if(href.contains(include1)&&href.contains(include2)&&href.contains(include3)){
                return true;
            }else{
                return false;
            }
        }
    }

    public static boolean filtrationHtml(String url,String a,String b,String c,String d,String e,String f){
        String trimUrl = url.replaceAll("\\d", "");
        if (a == null && b == null && c == null && d == null && e == null && f == null){
            return true;
        } else if (a !=null && b == null && c == null){
            if ( d == null && e == null && f == null) {
                if (trimUrl.indexOf(a) != -1){
                    return true;
                }
            } else if (d != null && e == null && f == null){
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(d) == -1){
                    return true;
                }
            } else if (d != null && e != null && f == null){
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(d) == -1 && trimUrl.indexOf(e) == -1){
                    return true;
                }
            } else if (d != null && e != null && f != null){
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(d) == -1 && trimUrl.indexOf(e) == -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e !=null && f != null) {
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(e) == -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e ==null && f != null){
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e !=null && f == null) {
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(e) == -1){
                    return true;
                }
            }
        } else if(a != null && b !=null && c == null){
            if ( d == null && e == null && f == null) {
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(b) != -1){
                    return true;
                }
            } else if (d != null && e == null && f == null){
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(b) != -1 && trimUrl.indexOf(d) == -1){
                    return true;
                }
            } else if (d != null && e != null && f == null){
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(b) != -1 && trimUrl.indexOf(d) == -1 && trimUrl.indexOf(e) == -1){
                    return true;
                }
            } else if (d != null && e != null && f != null){
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(b) != -1 && trimUrl.indexOf(d) == -1 && trimUrl.indexOf(e) == -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e !=null && f != null) {
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(b) != -1 && trimUrl.indexOf(e) == -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e ==null && f != null){
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(b) != -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e !=null && f == null) {
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(b) != -1 && trimUrl.indexOf(e) == -1){
                    return true;
                }
            }
        } else if(a != null && b !=null && c != null){
            if ( d == null && e == null && f == null) {
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(b) != -1 && url.indexOf(c) != -1){
                    return true;
                }
            } else if (d != null && e == null && f == null){
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(b) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(d) == -1){
                    return true;
                }
            } else if (d != null && e != null && f == null){
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(b) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(d) == -1 && trimUrl.indexOf(e) == -1){
                    return true;
                }
            } else if (d != null && e != null && f != null){
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(b) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(d) == -1 && trimUrl.indexOf(e) == -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e !=null && f != null) {
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(b) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(e) == -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e ==null && f != null){
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(b) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e !=null && f == null) {
                if (trimUrl.indexOf(a) != -1 && trimUrl.indexOf(b) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(e) == -1){
                    return true;
                }
            }
        }else if(a != null && b ==null && c != null){
            if ( d == null && e == null && f == null) {
                if (trimUrl.indexOf(a) != -1 && url.indexOf(c) != -1){
                    return true;
                }
            } else if (d != null && e == null && f == null){
                if (trimUrl.indexOf(a) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(d) == -1){
                    return true;
                }
            } else if (d != null && e != null && f == null){
                if (trimUrl.indexOf(a) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(d) == -1 && trimUrl.indexOf(e) == -1){
                    return true;
                }
            } else if (d != null && e != null && f != null){
                if (trimUrl.indexOf(a) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(d) == -1 && trimUrl.indexOf(e) == -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e !=null && f != null) {
                if (trimUrl.indexOf(a) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(e) == -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e ==null && f != null){
                if (trimUrl.indexOf(a) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e !=null && f == null) {
                if (trimUrl.indexOf(a) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(e) == -1){
                    return true;
                }
            }
        } else if(a == null && b !=null && c != null){
            if ( d == null && e == null && f == null) {
                if (trimUrl.indexOf(b) != -1 && url.indexOf(c) != -1){
                    return true;
                }
            } else if (d != null && e == null && f == null){
                if (trimUrl.indexOf(b) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(d) == -1){
                    return true;
                }
            } else if (d != null && e != null && f == null){
                if (trimUrl.indexOf(b) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(d) == -1 && trimUrl.indexOf(e) == -1){
                    return true;
                }
            } else if (d != null && e != null && f != null){
                if (trimUrl.indexOf(b) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(d) == -1 && trimUrl.indexOf(e) == -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e !=null && f != null) {
                if (trimUrl.indexOf(b) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(e) == -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e ==null && f != null){
                if (trimUrl.indexOf(b) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e !=null && f == null) {
                if (trimUrl.indexOf(b) != -1 && url.indexOf(c) != -1 && trimUrl.indexOf(e) == -1){
                    return true;
                }
            }
        } else if(a == null && b ==null && c != null){
            if ( d == null && e == null && f == null) {
                if (trimUrl.indexOf(c) != -1){
                    return true;
                }
            } else if (d != null && e == null && f == null){
                if (url.indexOf(c) != -1 && trimUrl.indexOf(d) == -1){
                    return true;
                }
            } else if (d != null && e != null && f == null){
                if (url.indexOf(c) != -1 && trimUrl.indexOf(d) == -1 && trimUrl.indexOf(e) == -1){
                    return true;
                }
            } else if (d != null && e != null && f != null){
                if (url.indexOf(c) != -1 && trimUrl.indexOf(d) == -1 && trimUrl.indexOf(e) == -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e !=null && f != null) {
                if (url.indexOf(c) != -1 && trimUrl.indexOf(e) == -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e ==null && f != null){
                if (url.indexOf(c) != -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e !=null && f == null) {
                if (url.indexOf(c) != -1 && trimUrl.indexOf(e) == -1){
                    return true;
                }
            }
        } else if(a == null && b !=null && c == null){
            if ( d == null && e == null && f == null) {
                if (trimUrl.indexOf(b) != -1){
                    return true;
                }
            } else if (d != null && e == null && f == null){
                if (trimUrl.indexOf(b) != -1 && trimUrl.indexOf(d) == -1){
                    return true;
                }
            } else if (d != null && e != null && f == null){
                if (trimUrl.indexOf(b) != -1 && trimUrl.indexOf(d) == -1 && trimUrl.indexOf(e) == -1){
                    return true;
                }
            } else if (d != null && e != null && f != null){
                if (trimUrl.indexOf(b) != -1 && trimUrl.indexOf(d) == -1 && trimUrl.indexOf(e) == -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e !=null && f != null) {
                if (trimUrl.indexOf(b) != -1 && trimUrl.indexOf(e) == -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e ==null && f != null){
                if (trimUrl.indexOf(b) != -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e !=null && f == null) {
                if (trimUrl.indexOf(b) != -1 && trimUrl.indexOf(e) == -1){
                    return true;
                }
            }
        } else if(a == null && b ==null && c == null){
            if (d != null && e == null && f == null){
                if (trimUrl.indexOf(d) == -1){
                    return true;
                }
            } else if (d != null && e != null && f == null){
                if (trimUrl.indexOf(d) == -1 && trimUrl.indexOf(e) == -1){
                    return true;
                }
            } else if (d != null && e != null && f != null){
                if (trimUrl.indexOf(d) == -1 && trimUrl.indexOf(e) == -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e !=null && f != null) {
                if (trimUrl.indexOf(e) == -1 && trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e ==null && f != null){
                if (trimUrl.indexOf(f) == -1){
                    return true;
                }
            } else if (d == null && e !=null && f == null) {
                if (trimUrl.indexOf(e) == -1){
                    return true;
                }
            }
        }
        return false;
    }


    public static String getAbsoluteURL(String curl, String file){

        URL url = null;
        String q = "";
        try {
            url = new   URL(new   URL(curl),file);
            q = url.toExternalForm();
        } catch (MalformedURLException e) {
            logger.info("当前href为无效连接==="+curl + file);
        }
        if(q.indexOf("#")!=-1)q = q.replaceAll("^(. ?)#.*?$", "$1");

        return q;

    }






    public static void main(String args[]){
        String curl = "http://beijing.pbc.gov.cn/beijing/132026/index.html";
        String file = "/beijing/132026/3772671/index.html";
        URL url = null;
        String q = "";
        try {
            url = new   URL(new   URL(curl),file);
            q = url.toExternalForm();
        } catch (MalformedURLException e) {

        }
        if(q.indexOf("#")!=-1)q = q.replaceAll("^(. ?)#.*?$", "$1");
        System.out.println(q);

        System.out.println(getAbsoluteURL2(curl,file));
    }



    public static String getAbsoluteURL2(String baseURI, String relativePath){

        String abURL="";
        try {
            if(!relativePath.contains("http:")) {//relativePath.contains("/")||relativePath.contains("//")
                URL absoluteUrl = new URL(baseURI);
                URL parseUrl = new URL(absoluteUrl, relativePath);
                abURL = parseUrl.toString();
            }
        } catch (MalformedURLException e) {
            return abURL;
        } finally{
            return abURL;
        }



    }

}
