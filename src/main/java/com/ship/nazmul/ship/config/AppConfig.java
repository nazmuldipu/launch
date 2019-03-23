package com.ship.nazmul.ship.config;

import com.ship.nazmul.ship.interceptors.ActivityInterceptor;
import com.ship.nazmul.ship.listeners.AuthenticationFailureEventListener;
import com.ship.nazmul.ship.listeners.AuthenticationSuccessEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {

    private final AuthenticationSuccessEventListener successEventListener;
    private final AuthenticationFailureEventListener failureEventListener;
    private final ActivityInterceptor activityInterceptor;

    @Autowired
    public AppConfig(AuthenticationSuccessEventListener successEventListener, AuthenticationFailureEventListener failureEventListener, ActivityInterceptor activityInterceptor) {
        this.successEventListener = successEventListener;
        this.failureEventListener = failureEventListener;
        this.activityInterceptor = activityInterceptor;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setDispatchOptionsRequest(true);
        return dispatcherServlet;
    }


    @Bean
    public ApplicationListener loginSuccessListener() {
        return this.successEventListener;
    }

    @Bean
    public ApplicationListener loginFailureListener() {
        return this.failureEventListener;
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    // REGISTER INTERCEPTER
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(activityInterceptor);
    }
}
