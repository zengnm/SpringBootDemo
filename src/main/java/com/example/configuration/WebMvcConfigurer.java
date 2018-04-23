package com.example.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.util.WebHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigurer implements WebMvcConfigurer {
    /**
     * 多个拦截器组成一个拦截器链
     * addPathPatterns 用于添加拦截规则，excludePathPatterns 用户排除拦截
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new HandlerInterceptor() {
            //在请求处理之前进行调用（Controller方法调用之前）
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                    throws Exception {
                String pin = WebHelper.getPin();
                if (StringUtils.isEmpty(pin)) {
                    response.sendRedirect(request.getContextPath() + "/login");
                    return false;
                }
                return true;// 只有返回true才会继续向下执行，返回false取消当前请求
            }

            //请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                                   ModelAndView modelAndView) throws Exception {
            }

            //在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                        Exception ex) throws Exception {
            }
        }).addPathPatterns("/**").excludePathPatterns("/login", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg",
                "/**/*.jpeg", "/**/fonts/*");
    }
}
