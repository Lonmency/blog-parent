package com.lon.blog.config;

import com.lon.blog.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        //跨域配置
//        //从localhost:8080到localhost:8888就是跨域
//        //允许localhost:8080的请求访问后端服务的所有路径
//        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
//    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截test接口，后续实际遇到需要拦截的接口时，在配置为真正的拦截接口
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/test")
                //评论，评论的时候必须要登录，所以进行登录拦截
                .addPathPatterns("/comments/create/change")
                .addPathPatterns("/articles/publish");
    }

//    /**
//     * springmvc视图解析器配置
//     * @Title: viewResolver
//     * @Description: TODO
//     * @Date 2018年8月28日 下午4:46:07
//     * @author OnlyMate
//     * @return
//     */
//    @Bean
//    public InternalResourceViewResolver viewResolver(){
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setPrefix("/BOOT-INF/classes/static/");
//        viewResolver.setSuffix(".html");
//        // viewResolver.setViewClass(JstlView.class); // 这个属性通常并不需要手动配置，高版本的Spring会自动检测
//        return viewResolver;
//    }
//
//    /**
//     * SpringBoot设置首页
//     */
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        WebMvcConfigurer.super.addViewControllers(registry);
//        registry.addViewController("/login/1").setViewName("login");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }
}
