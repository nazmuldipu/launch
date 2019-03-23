package com.ship.nazmul.ship;

import com.ship.nazmul.ship.commons.utils.PasswordUtil;
import com.ship.nazmul.ship.services.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class ShipApplication {
	private final CustomUserDetailsService userDetailsService;

	@Autowired
	public ShipApplication(CustomUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public static void main(String[] args) {
		SpringApplication.run(ShipApplication.class, args);
	}

	@Autowired
	public void authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(userDetailsService).passwordEncoder(PasswordUtil.getBCryptPasswordEncoder());
	}

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+06:00"));
		System.out.println("====== Timezone set to Dhaka ======");
	}
}


