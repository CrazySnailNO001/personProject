package com.xzh.personalproject.commons.utils;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Properties;

/**
 * @author XZHH
 * @Description:
 * @create 2018/8/22 0022 11:43
 * @modify By:
 **/
public class JpushUtil {
    private static final Logger log = LoggerFactory.getLogger(JpushUtil.class);

    private static String masterSecret;
    private static String appKey;
    private static String ALERT;
    private static String ALIAS;
    private static String TAG;
    private static String plantform_android;
    private static String plantform_ios;

    public JpushUtil() {
        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("jpush.properties"), "utf-8"));
            masterSecret = prop.getProperty("masterSecret");
            appKey = prop.getProperty("appKey");
            ALERT = prop.getProperty("ALERT");
            ALIAS = prop.getProperty("ALIAS");
            TAG = prop.getProperty("TAG");
            plantform_android = prop.getProperty("plantform_android");
            plantform_ios = prop.getProperty("plantform_ios");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        jiguangPush();
    }

    /**
     * 极光推送
     */
    public static void jiguangPush() {
        new JpushUtil();
        log.info("对别名" + ALIAS + "的用户推送信息");

        //安卓 ios一起发送
        PushResult androidResult = push(plantform_android, String.valueOf(ALIAS), String.valueOf(TAG), ALERT);
//        PushResult iosResult = push(plantform_ios, String.valueOf(ALIAS), String.valueOf(TAG), ALERT);
        if (androidResult != null && androidResult.isResultOK()) {
            log.info("针对别名" + ALIAS + "的安卓信息推送成功！");
        } else {
            log.info("针对别名" + ALIAS + "的安卓信息推送失败！");
        }
    }

    public static void setUser() {
        HashSet<String> tags = new HashSet<>();
        tags.add("粮顺通");
        tags.add("管理员");
//        JPushInterface
    }

    /**
     * 生成极光推送对象PushPayload（采用java SDK）
     *
     * @param alias
     * @param alert
     * @return PushPayload
     */
    public static PushPayload buildPushObject_android_ios_alias_tag_alert(String alias, String tag, String alert) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())//android ios 平台
//                .setPlatform(Platform.android())
//                .setAudience(Audience.alias(alias))
//                .setAudience(Audience.tag(tag))
                .setAudience(Audience.all())//项目中的所有用户
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .addExtra("type", "infomation")
                                .setAlert(alert)
                                .build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .addExtra("type", "infomation")
                                .setAlert(alert)
                                .build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(false)//true-推送生产环境 false-推送开发环境（测试使用参数）
                        .setTimeToLive(90)//消息在JPush服务器的失效时间（测试使用参数）
                        .build())
                .build();
    }

    public static PushPayload buildPushObject_ios_alias_tag_alert(String alias, String tag, String alert) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.alias(alias))
                .setAudience(Audience.tag(tag))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .addExtra("type", "infomation")
                                .setAlert(alert)
                                .build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(false)//true-推送生产环境 false-推送开发环境（测试使用参数）
                        .setTimeToLive(90)//消息在JPush服务器的失效时间（测试使用参数）
                        .build())
                .build();
    }

    /**
     * 极光推送方法(采用java SDK)
     *
     * @param alias
     * @param alert
     * @return PushResult
     */
    public static PushResult push(String plantform, String alias, String tag, String alert) {
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
        PushPayload payload;
        payload = buildPushObject_android_ios_alias_tag_alert(alias, tag, alert);
        try {
            PushResult pushResult = jpushClient.sendPush(payload);
            return jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            return null;
        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            return null;
        }
    }

}
