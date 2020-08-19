package com.viettelpost.api.base;

import org.apache.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.Date;

//@Component
public class ViettelPostFilter extends OncePerRequestFilter {
    Logger logger = Logger.getLogger(this.getClass());

    private static final String key = "viettelpost-final-key";
    private static final String time = "viettelpost-final-time";
    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"};

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filter) throws ServletException, IOException {
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(res) {
            @Override
            public void setStatus(int sc) {
                super.setStatus(sc);
                handleStatus(sc);
            }

            @Override
            @SuppressWarnings("deprecation")
            public void setStatus(int sc, String sm) {
                super.setStatus(sc, sm);
                handleStatus(sc);
            }

            @Override
            public void sendError(int sc, String msg) throws IOException {
                super.sendError(sc, msg);
                logger.info(req.getHeader("Token"));
                handleStatus(sc);
            }

            @Override
            public void sendError(int sc) throws IOException {
                super.sendError(sc);
                logger.info(req.getHeader("Token"));
                handleStatus(sc);
            }

            private void handleStatus(int code) {
                logger.info(buildData(req, res, "OUT"));
            }
        };
        logger.info(buildData(req, res, "IN"));
        filter.doFilter(req, wrapper);
    }


    private String buildData(HttpServletRequest req, HttpServletResponse res, String prefix) {
        if (prefix.equals("IN")) {
            req.setAttribute(time, new Date().getTime());
        }
        Long old = (Long) req.getAttribute(time);
        StringBuilder builder = new StringBuilder();

        builder.append(req.getRequestURL()).append(" | ").append(req.getMethod()).append(" | ").append(new Date().getTime());
        if (prefix.equals("OUT")) {
            builder.append(" | ").append(res.getStatus()).append(" | ").append(new Date().getTime() - old);
        }

        return builder.toString();
    }
}
