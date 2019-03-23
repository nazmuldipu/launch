package com.ship.nazmul.ship.interceptors;

import com.ship.nazmul.ship.commons.utils.NetworkUtil;
import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.Activity;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.services.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ActivityInterceptor extends HandlerInterceptorAdapter {

    private final ActivityService activityService;

    @Autowired
    public ActivityInterceptor(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) userAgent = request.getHeader("user-agent");
        String expires = response.getHeader("Expires");
        Activity activity = new Activity();
        activity.setIp(NetworkUtil.getClientIP());
        activity.setExpires(expires);
        activity.setRequestMethod(request.getMethod());
        activity.setUrl(request.getRequestURI());

        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(userAgent);
        if (m.find()) {
            activity.setUserAgent(m.group(1));
        }

        if (SecurityConfig.isAuthenticated()) {
            User user = SecurityConfig.getCurrentUser();
            activity.setUser(user);
            if (!activity.getUrl().contains("image") && !activity.getUrl().equals("/"))
                activity = activityService.save(activity);
            return super.preHandle(request, response, handler);
        }
        return super.preHandle(request, response, handler);
    }

}
