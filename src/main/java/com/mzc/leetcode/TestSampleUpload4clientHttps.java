package com.mzc.leetcode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TestSampleUpload4clientHttps {
    public static void main(String[] args)  {

        try(BufferedReader reader = Files.newBufferedReader(Paths.get("D:\\电话号\\aaa.txt"), Charset.forName("utf-8"))) {
            String s = null;
            int count = 0;
            while ((s = reader.readLine()) != null) {
                String[] tmp = s.split(",");
                try {
                    count ++;
                    System.out.print(searchModileLocation(tmp[2], tmp[0], tmp[1]));
                    break;
//                    http://www.ip138.com:8080/search.asp?mobile=18611812654&action=mobile
//                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (Exception e){
                    e.printStackTrace();
                    System.out.printf("%s,%s,%s,查询异常\n", tmp[0], tmp[1], tmp[2]);
                    break;
                }
            }
            System.out.println(count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String searchModileLocation(String mobile, String number, String name) throws Exception{
        CloseableHttpClient closeableHttpClient = createHttpsClient();
        // 建立HttpPost对象
//        HttpPost httppost = new HttpPost("https://shouji.51240.com/"+mobile+"__shouji/");
        HttpPost httppost = new HttpPost("http://www.1234i.com/p.php");//?mobile="+mobile+"&action=mobile");

        // 配置要 POST 的数据begin
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        // 设置为浏览器兼容模式
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        // 设置请求的编码格式
        multipartEntityBuilder.setCharset(Charset.forName("gb2312"));
        ContentType TEXT_PLAIN = ContentType.create("text/plain", Charset.forName("gb2312"));
//        multipartEntityBuilder.addTextBody("userName", "admin", TEXT_PLAIN);
//        multipartEntityBuilder.addTextBody("psd", "admin", TEXT_PLAIN);
//        multipartEntityBuilder.addTextBody("mac", "abw3232jjf2swsj3", TEXT_PLAIN);
//        multipartEntityBuilder.addTextBody("md5", "uy0kfwefess8e6", TEXT_PLAIN);
//        multipartEntityBuilder.addTextBody("type", "sample", TEXT_PLAIN);
        multipartEntityBuilder.addTextBody("haomas", "18611812654", TEXT_PLAIN);
        multipartEntityBuilder.addTextBody("submit", "Submit", TEXT_PLAIN);
        //文件路径
//        File file = new File("D:\\aaa.txt");
//        multipartEntityBuilder.addBinaryBody("file", file);
        // 配置要 POST 的数据end
        // 生成 HTTP POST 实体
        HttpEntity httpEntity = multipartEntityBuilder.build();
        httppost.setEntity(httpEntity);
        //发送Post,并返回一个HttpResponse对象
        HttpResponse httpResponse = closeableHttpClient.execute(httppost);
        HttpEntity httpEntity2 = httpResponse.getEntity();
        // 如果状态码为200,就是正常返回
        System.out.println("return code:" + httpResponse.getStatusLine().getStatusCode());
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String result = EntityUtils.toString(httpEntity2);
//            String r = parseEntityBody(result, mobile, number, name);
            String r = parse2345(result, mobile, number, name);
            return r;
            // 得到返回的字符串
//            System.out.println(result);
            // 如果是下载文件,可以用response.getEntity().getContent()返回InputStream
        } else {
            String result = EntityUtils.toString(httpEntity2);
            // 得到返回的字符串
            System.out.println(result);
        }
        //关闭连接
        closeableHttpClient.close();
        return String.format("22没有找到对应信息,%s\n", mobile);
    }

    public static String parseEntityBody(String entityBody, String mobileNumber, String number, String name) {
        System.out.println(entityBody);
        int startIndex = entityBody.indexOf("<title>");
        int endIndex = entityBody.indexOf("</title>");
        String r = String.format("%s,%s,%s,没有找到数据\n", number, name, mobileNumber);
        if (startIndex > 0 && endIndex > startIndex) {
            String s = entityBody.substring(startIndex + 7, endIndex);
            String[] ss = s.split(" ");
            if (ss.length >= 3) {
                if (mobileNumber.trim().equals(ss[0].trim())) {
                    r = String.format("%s,%s,%s,%s,%s\n", number, name, ss[0].trim(), ss[1].trim(), ss[2].trim());
                } else {
                    r = String.format("%s,%s,%s,%s,%s,%s,电话号码不匹配\n", number, name, mobileNumber, ss[0].trim(), ss[1].trim(), ss[2].trim());
                }
            }
        }
        return r;
    }

    public static String parse2345(String entityBody, String mobileNumber, String number, String name) {
        System.out.println(entityBody);
        if (true) {
            return "";
        }
        int fromIndex = 0;
        String[] tmp = new String[3];
        for (int i = 0; i < 4; i++) {
            fromIndex = entityBody.indexOf("<strong>", fromIndex) + 1;
            if (i-1 >= 0) {
                tmp[i-1] = entityBody.substring(fromIndex+7, entityBody.indexOf("</strong>", fromIndex));
            }
        }
        if(tmp[0] == null || tmp[0].isEmpty()) {
            throw new RuntimeException();
        }
//        System.out.println("============" + Arrays.toString(tmp));
//        System.out.println("*************");
        return String.format("%s,%s,%s,%s,%s\n", number, name, tmp[0].trim(), tmp[1].trim(), tmp[2].trim());
    }

    public static CloseableHttpClient createHttpsClient() throws Exception {
        X509TrustManager x509mgr = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] xcs, String string) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] xcs, String string) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{x509mgr}, new java.security.SecureRandom());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslContext,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }
}
