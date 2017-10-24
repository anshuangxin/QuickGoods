package com.zmsoft.TestTool.utils.okhttp.cookie;

import java.util.ArrayList;
import java.util.List;

import com.zmsoft.TestTool.utils.Logger;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public final class SimpleCookieJar implements CookieJar {
    private final List<Cookie> allCookies = new ArrayList<>();

    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        allCookies.addAll(cookies);
        for (Cookie c : cookies) {
            Logger.log("cookie: " + url + "----" + c.toString());
        }
    }

    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        Logger.log("cookie: " + url.toString());
        List<Cookie> result = new ArrayList<>();
        for (Cookie cookie : allCookies) {
            if (cookie.matches(url)) {
                result.add(cookie);
            }
        }
        return result;
    }
}
