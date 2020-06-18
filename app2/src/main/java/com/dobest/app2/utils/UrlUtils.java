package com.dobest.app2.utils;

import javax.servlet.http.HttpServletRequest;

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
