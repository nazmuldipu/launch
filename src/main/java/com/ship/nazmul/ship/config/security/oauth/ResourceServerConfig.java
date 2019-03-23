package com.ship.nazmul.ship.config.security.oauth;

import com.ship.nazmul.ship.entities.Role;
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
                        "/api/v1/users/create",
                        "/api/v1/ships/**",
                        "/api/v1/category/**",
                        "/api/v1/seats/**",
                        "/api/v1/feedback/**",
                        "/login**",
                        "/oauth/token**",
                        "/sw/**",
                        "/resetPassword/**",
                        "/checkTokenValidity",
                        "/proxy/**"

                )
                .permitAll()
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