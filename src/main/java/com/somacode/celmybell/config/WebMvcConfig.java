package com.somacode.celmybell.config;

import com.somacode.celmybell.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired RequestTimeInterceptor requestTimeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestTimeInterceptor);
    }

    @Component
    public static class RequestTimeInterceptor extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            request.setAttribute("requestTime", System.currentTimeMillis());
            return true;
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
            long time = (long) request.getAttribute("requestTime");
            String endpoint = request.getServletPath();
            if (endpoint.startsWith("/bower_components")
                    || endpoint.startsWith("/swagger-resources")
                    || endpoint.startsWith("/favicon")
                    || endpoint.startsWith("/dist")
                    || endpoint.startsWith("/webjars")
                    || endpoint.startsWith("/null")
                    || endpoint.startsWith("/plugins")) {
                return;
            }

            String methodHttp = request.getMethod();
            int executionTime = (int) (System.currentTimeMillis() - time);
            String content = methodHttp + "\t" + "'" + endpoint + "'" + "\t in " + executionTime + " ms";

            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }

            LogService.out.endpoint(content, executionTime, methodHttp, endpoint, ipAddress);
        }
    }

    // CON ESTO MANEJAMOS EL CORS
    @Component
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public static class CorsConfig implements Filter {

        private static final String URL_CORS_ORIGIN = "*";
        private static final String URL_CORS_METHODS = "*";


        @Override
        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
            HttpServletResponse response = (HttpServletResponse) res;
            HttpServletRequest request = (HttpServletRequest) req;
            response.setHeader("Access-Control-Allow-Origin", URL_CORS_ORIGIN);
            response.setHeader("Access-Control-Allow-Methods", URL_CORS_METHODS);
            response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Authorization, Content-Type");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Expose-Headers", "X-Total-Count");

            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                chain.doFilter(req, res);
            }
        }

    }
}
