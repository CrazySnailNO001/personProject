package com.xzh.personalproject.commons.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HttpClientUtils {

    // HTTP内容类型。相当于form表单的形式，提交数据
    public static final String CONTENT_TYPE_JSON_URL = "application/json;charset=utf-8";

    // utf-8字符编码
    public static final String CHARSET_UTF_8 = "utf-8";

    // 连接管理器
    private static PoolingHttpClientConnectionManager pool;

    // 请求配置
    private static RequestConfig requestConfig;


    public static CloseableHttpClient getHttpClient() {

        CloseableHttpClient httpClient = HttpClients.custom()
                // 设置连接池管理
                .setConnectionManager(pool)
                // 设置请求配置
                .setDefaultRequestConfig(requestConfig)
                // 设置重试次数
                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                .build();

        return httpClient;
    }

    /**
     * 构建请求配置信息
     * 超时时间什么的
     */
    private static RequestConfig requestConfig() {
        // 根据默认超时限制初始化requestConfig
        int socketTimeout = 10000;
        int connectTimeout = 10000;
        int connectionRequestTimeout = 10000;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(connectTimeout) // 创建连接的最长时间
                .setConnectionRequestTimeout(connectionRequestTimeout) // 从连接池中获取到连接的最长时间
                .setSocketTimeout(socketTimeout) // 数据传输的最长时间
                .build();
        return config;
    }

    public static String doGetJson(String url, String param, Map<String, String> requestHead) {
        String result = "";
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = null;
        try {
            httpclient = getHttpClient();
            URI uri = null;
            if (param == null || param.equals("")) {
                uri = new URIBuilder(url).build();
            } else {
                uri = new URIBuilder(url + "?" + param).build();
            }
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setConfig(requestConfig());
            ifNone(requestHead, httpGet);
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), CHARSET_UTF_8);
            }
            result = decodeData(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                //不可以关闭，不然连接池就会被关闭
                //httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static void ifNone(Map<String, String> requestHead, HttpGet httpGet) {
        if (null != requestHead) {
            for (Map.Entry<String, String> entry : requestHead.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                httpGet.addHeader(key, value);
            }
        }
    }


    public static String doPostJson(String url, String param, Map<String, String> requestHead) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(param, ContentType.APPLICATION_JSON);
            entity.setContentType(CONTENT_TYPE_JSON_URL);
            httpPost.setEntity(entity);
            httpPost.setConfig(requestConfig());
            if (null != requestHead) {
                for (Map.Entry<String, String> entry : requestHead.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    httpPost.addHeader(key, value);
                }
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), CHARSET_UTF_8);
            }
            resultString = decodeData(resultString);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }


    public static String doGet(String url, Map<String, String> param, Map<String, String> requestHead) {
        String result = "";
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = null;
        try {
            String params = toHttpGetParams(param);
            httpclient = getHttpClient();
            URI uri = new URIBuilder(url + "?" + params).build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setConfig(requestConfig());
            ifNone(requestHead, httpGet);
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), CHARSET_UTF_8);
            }
            result = decodeData(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String doGet(String url, Map<String, String> param) {
        return doGet(url, param, null);
    }


    /**
     * 根据实际需要决定是否需要解码
     */
    static String decodeData(String base64Data) {
        String str = "";
        if (base64Data == null || base64Data.equals("")) {
            str = "";
        }
        try {
            String e = new String(Base64.decodeBase64(base64Data.getBytes(CHARSET_UTF_8)), CHARSET_UTF_8);
            return e;
        } catch (UnsupportedEncodingException var5) {
        }
        return str;
    }

    /**
     * 这里只是其中的一种场景,也就是把参数用&符号进行连接且进行URL编码
     * 根据实际情况拼接参数
     */
    private static String toHttpGetParams(Map<String, String> param) throws Exception {
        String res = "";
        if (param == null) {
            return res;
        }
        for (Map.Entry<String, String> entry : param.entrySet()) {
            res += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), CHARSET_UTF_8) + "&";
        }
        return "".equals(res) ? "" : StringUtils.chop(res);
    }


    public static String doPost(String url, Map<String, String> param, Map<String, String> requestHead) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig());
            if (null != requestHead) {
                for (Map.Entry<String, String> entry : requestHead.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    httpPost.addHeader(key, value);
                }
            }
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), CHARSET_UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static String doPost(String url, Map<String, String> param) {

        return doPost(url, param, null);
    }

    public static String doPost(String url) {
        return doPost(url, null);
    }

    public static String sendPost(String url, String data) {
        String response = null;
        //  log.info("url: " + url);
        //  log.info("request: " + data);
        try {
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse httpresponse = null;
            try {
                httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost(url);
                StringEntity stringentity = new StringEntity(data,
                        ContentType.create("text/json", "UTF-8"));
                httppost.setEntity(stringentity);
                httpresponse = httpclient.execute(httppost);
                response = EntityUtils
                        .toString(httpresponse.getEntity());
                //log.info("response: " + response);
            } finally {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (httpresponse != null) {
                    httpresponse.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


}