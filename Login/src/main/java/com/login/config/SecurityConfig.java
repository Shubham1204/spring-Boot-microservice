package com.login.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.login.filter.JwtRequestFilter;
import com.login.service.CustomUserDetailsService;

@Component
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}

	 private static final String[] AUTH_WHITELIST = {
	            // -- Swagger UI v2
	            "/v2/api-docs",
	            "/swagger-resources",
	            "/swagger/**",
	            "/swagger-resources/**",
	            "/configuration/ui",
	            "/configuration/security",
	            "/swagger-ui.html",
	            "/webjars/**",
	            // -- Swagger UI v3 (OpenAPI)
	            "/v3/api-docs/**",
	            "/swagger-ui/**",
	            // actuator
	            "/actuator/**",
	            // other public endpoints
	            "/public/**"
	    };
	 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		
		.authorizeRequests()
//		.authorizeRequests().antMatchers(HttpMethod.DELETE).hasRole(Role.ADMIN)
		.antMatchers("/authenticated/admin/**").hasAuthority("admin")
		.antMatchers("/authenticated/**").hasAnyAuthority("user","admin")
				.antMatchers(AUTH_WHITELIST).permitAll()
		.anyRequest().authenticated()
//		.and().oauth2Login()
		.and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder(10);
		return NoOpPasswordEncoder.getInstance();
	}
	
	public String passwordHash(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(8));
	}
}
