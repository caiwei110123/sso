package com.skrein.app1.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class UrlUtils {

    public static String buildHostUrl(HttpServletRequest request) {
        // get header remote url
        String host = request.getHeader("Host");
        if (host == null || "".equals(host)) {
            return request.getRequestURL().toString();
        }

        return "http://" +
                host +
                request.getRequestURI();
    }

}
