package com.xzh.personalproject.commons.utils;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Cookie控制 支持网站Cookie信息的写入、修改和删除
 *
 * @author CLion
 */
public class CookieCtrl {

    /**
     * 本地写入Cookie
     *
     * @param response
     * @param name
     * @param value    不能包含中文
     * @param expiry   cookie失效时间
     * @param domain   cookie跨域访问的域名
     */
    public static void setCookieInfo(HttpServletResponse response, String name, String value, int expiry, String domain) {
        Cookie cookie = new Cookie(name, value);

        cookie.setMaxAge(expiry);

        // domain不能使用".localhost"，此域在火狐下正常，在IE下不正常
        // 跨域共享设置时，如：A机所在域:my.test.com;B机所在域：page.test.com.共享cookie所设置的域为：".test.com"
        if (domain != null && "".equals(domain) == false)
            cookie.setDomain(domain);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 本地写入内存Cookie,浏览器关闭时清除
     *
     * @param response
     * @param name
     * @param value    可以包含中文
     * @param domain   cookie跨域访问的域名,如果在多个地方用不同的方式访问,请保持其一致有效
     */
    public static void setMemCookieInfo(HttpServletResponse response, String name, String value, String domain) {
        try {
            // 解决Cookie中保存中文
            value = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // "-1"表示当关闭浏览器时，该 cookie被清除
        setCookieInfo(response, name, value, -1, domain);
    }

    /**
     * 本地写入内存Cookie,浏览器关闭时清除
     *
     * @param response
     * @param name
     * @param value    可以包含中文
     * @param domain   cookie跨域访问的域名,如果在多个地方用不同的方式访问,请保持其一致有效
     */
    public static void setMemCookieInfo(HttpServletResponse response, String name, String value, String domain, int auto) {
        try {
            // 解决Cookie中保存中文
            value = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // "-1"表示当关闭浏览器时，该 cookie被清除
        if (auto == 0)
            setCookieInfo(response, name, value, -1, domain);
        else {
            int exp = 365 * 24 * 3600;
            setCookieInfo(response, name, value, exp, domain);
        }
    }

    /**
     * 本地删除Cookie
     *
     * @param response
     * @param key
     * @param response
     */
    public static void delCookieInfo(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);    // 0 表示必须立即清除该 cookie
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 读取Cookie
     *
     * @param request
     * @param key
     * @return
     */
    public static String getCookieInfo(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key))
                    return cookie.getValue();
            }
        }
        return null;
    }
}
