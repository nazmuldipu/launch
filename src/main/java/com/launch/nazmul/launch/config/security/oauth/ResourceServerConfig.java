package com.launch.nazmul.launch.config.security.oauth;

import com.launch.nazmul.launch.entities.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    // TODO: secure url perfectly (some url need to secure at method level because of RequestMethod POST, PUT related to this url)
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
//                .antMatcher("/api/v1/**")
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/register/**",
                        "/dev/register",
                        "/dev/client/credentials",
                        "/api/v1/users/create",
                        "/api/v1/orders/create",
                        "/searchHotel/**",
                        "/api/v1/hotels/**",
                        "/api/v1/category/**",
                        "/api/v1/package/**",
                        "/api/v1/rooms/**",
                        "/payment/**",
                        "/api/v1/feedback/**",
                        "/login**",
                        "/oauth/token**",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/sw/**",
                        "/resetPassword/**",
                        "/checkTokenValidity",
                        "/api/v1/apps/all",
                        "/api/v1/promos/**",
                        "/swagger-ui.html",
                        "/proxy/**"

                )
                .permitAll()
//                .antMatchers(
//                        "/api/v1/search/users",
//                        "/api/v1/stats/employee"
//                )
//                .hasAnyAuthority(Role.StringRole.ROLE_ADMIN, Role.StringRole.ROLE_EMPLOYEE, Role.StringRole.ROLE_FIELD_EMPLOYEE)
                .antMatchers(
                        "/api/v1/admin/**"
                )
                .hasAnyAuthority(Role.StringRole.ROLE_ADMIN)
                .antMatchers("/api/v1/serviceAdmin/**")
                .hasAnyAuthority(Role.StringRole.ROLE_SERVICE_ADMIN)
                .antMatchers(
                        "/api/v1/users"
                )
                .hasAuthority(Role.StringRole.ROLE_ADMIN)
                .antMatchers("/api/v1/bookings/**")
                .authenticated()
                .anyRequest()
                .authenticated()
                .and().logout().logoutSuccessUrl("/").permitAll();
    }
}