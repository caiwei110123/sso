package com.skrein.app1.interceptor;


import com.skrein.app1.utils.HttpUtils;
import com.skrein.app1.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 2020/5/7 13:48
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Slf4j
public class LoginFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        // 判断是否登陆
        Boolean isLogin = (Boolean) servletRequest.getSession().getAttribute("is_login");

        // do interceptor static resources
        if (
                ((HttpServletRequest) request).getRequestURL().toString().endsWith(".css") ||
                        ((HttpServletRequest) request).getRequestURL().toString().endsWith(".js") ||
                        ((HttpServletRequest) request).getRequestURL().toString().endsWith(".ico")
        ) {
            chain.doFilter(request, response);
            return;
        }

        if (isLogin != null && isLogin) {
            chain.doFilter(request, response);
            return;
        }

        String ticket = getTicket(servletRequest);
        if (StringUtils.isEmpty(ticket)) {
            redirect2Login(servletRequest, servletResponse);
            return;
        }
        // 需要校验ticket
        String ticketResult = HttpUtils.doGet("http://sso.skrein.com/ticketValid?ticket=" + ticket);
        if ("ok".equals(ticketResult)) {
            setLoginStatus(servletRequest);
            redirect2Resoruce(servletRequest, servletResponse);
        } else {
            redirect2Login(servletRequest, servletResponse);
        }

    }

    private void redirect2Resoruce(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        servletResponse.setStatus(HttpServletResponse.SC_FOUND);
        servletResponse.setHeader("Location", UrlUtils.buildHostUrl(servletRequest));
    }

    private void setLoginStatus(HttpServletRequest servletRequest) {
        servletRequest.getSession().setAttribute("is_login", true);
    }

    private String getTicket(HttpServletRequest servletRequest) {
        return servletRequest.getParameter("ticket");
    }

    private void redirect2Login(HttpServletRequest servletRequest, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", "http://sso.skrein.com/index/login?service=" + UrlUtils.buildHostUrl(servletRequest));
    }


}
