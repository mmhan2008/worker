package com.cnstock.utils;

import com.cnstock.entity.TbJob;
import com.cnstock.entity.Worker;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2019/1/3.
 */
public class ReadUtil {
    public static BlockingQueue readFile(BlockingQueue<TbJob> workers) {
        String projectPath = new File("").getAbsolutePath();
        String pathname = projectPath + "/config/http.txt";
        try (
             FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                TbJob worker =new TbJob();
                if(line.contains("##")){
                    worker.setJobUrl(line.split("##")[1]);
                    worker.setJobName(line.split("##")[0]);
                }else{
                    worker.setJobName("上证报");
                    worker.setJobUrl(line);
                }

                worker.setJobId(UUID.randomUUID().toString().replaceAll("-", ""));
                workers.offer(worker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workers;
    }

    public static int readFile() {
        String projectPath = new File("").getAbsolutePath();
        String pathname = projectPath + "/config/http.txt";
        int b=0;
        try (
                FileReader reader = new FileReader(pathname);
                BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            int i= br.read();

            while ((line = br.readLine()) != null) {
                b++;
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static List<String> ReadUserAgent(){
        String projectPath = new File("").getAbsolutePath();
        String pathname = projectPath + "/config/UserAgent.txt";
        List<String> ua = new ArrayList<>();
        try (
                FileReader reader = new FileReader(pathname);
                BufferedReader br = new BufferedReader(reader)
        ) {
            String line;

            while ((line = br.readLine()) != null) {
                ua.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ua;
    }

    /**
     * 创建文件
     * @return  是否创建成功，成功则返回true
     */
    public static boolean createFile(TbJob worker){
        Boolean bool = false;
        String projectPath = new File("").getAbsolutePath();
        String filenameTemp = projectPath + "/logs/html/"+worker.getJobId()+".txt";

        File file = new File(filenameTemp);
        try {
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
                file.createNewFile();
                bool = true;
                //创建文件成功后，写入内容到文件里
                writeFileContent(filenameTemp, worker.getContent());
            }else{
                writeFileContent(filenameTemp, worker.getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bool;
    }

    /**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr+"\r\n";//新写入的行，换行

        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();

            buffer.append(filein);

            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }



    public static void main(String[] args) {
//        BlockingQueue<Worker> workers= new ArrayBlockingQueue<Worker>(ReadUtil.readFile());
//        System.out.println(ReadUtil.ReadUserAgent().size());

//        long start = System.currentTimeMillis();
//        System.out.println(ReadUtil.ReadUserAgent().size());
//        long end = System.currentTimeMillis();
//        System.out.println((end-start));

        try {
            String ss =  URLEncoder.encode ("", "UTF-8" );
            System.out.println(ss);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        System.out.println(s);
    }


    //字符串转换unicode
    public static String stringToUnicode(String str) {
        String prefix = "\\u";
        StringBuffer sb = new StringBuffer();
        char[] chars = str.toCharArray();
        if (chars == null || chars.length == 0) {
            return null;
        }
        for (char c : chars) {
            sb.append(prefix);
            sb.append(Integer.toHexString(c));
        }
        return sb.toString();
    }

    //unicode 转字符串
    public static String unicodeToString(String str) {
        String sg = "\\u";
        int a = 0;
        List<String> list = new ArrayList<>();
        while (str.contains(sg)) {
            str = str.substring(2);
            String substring;
            if (str.contains(sg)) {
                substring = str.substring(0, str.indexOf(sg));
            } else {
                substring = str;
            }
            if (str.contains(sg)) {
                str = str.substring(str.indexOf(sg));
            }
            list.add(substring);
        }
        StringBuffer sb = new StringBuffer();
        if (!CollectionUtils.isEmpty(list)){
            for (String string : list) {
                sb.append((char) Integer.parseInt(string, 16));
            }
        }
        return sb.toString();
    }






}
