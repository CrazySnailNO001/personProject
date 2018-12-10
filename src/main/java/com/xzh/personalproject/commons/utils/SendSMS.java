package com.xzh.personalproject.commons.utils;

import com.xzh.personalproject.commons.Global;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;

public class SendSMS {

    //短信接口地址
    private static String Url = "http://sdk.entinfo.cn:8061/webservice.asmx/mdsmssend";

    public static Integer send(String mobile) {
        if (Global.DEBUG)
            return 123456;

        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(Url);

        client.getParams().setContentCharset("UTF-8");
        method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");

        int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);
        String content = new String("您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。");

        NameValuePair[] data = {//封装参数
                new NameValuePair("sn", "SDK-BBX-010-27210"),
                //密码可以使用明文密码或使用32位MD5加密
                new NameValuePair("pwd", MD5.md5("SDK-BBX-010-272108#eb-9f2c-4").toUpperCase()),
                new NameValuePair("mobile", mobile),
                new NameValuePair("content", content),
                new NameValuePair("ext", ""),
                new NameValuePair("stime", ""),
                new NameValuePair("rrid", ""),
                new NameValuePair("msgfmt", "")
        };

        method.setRequestBody(data);

        try {
            client.executeMethod(method);
            //获取返回的Xml
            String SubmitResult = method.getResponseBodyAsString();

            //解析Xml
            Document doc = DocumentHelper.parseText(SubmitResult);
            Element root = doc.getRootElement();

            Long returnValue = Long.parseLong(root.getText());
            //对照返回值信息 为负数就是出现错误，>0说明发送成功（具体看返回的消息进行解析）
            //返回生成的验证码与用户输入的进行验证
            if (returnValue > 0) {
                return mobile_code;
            } else {
                return null;
            }

        } catch (HttpException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}