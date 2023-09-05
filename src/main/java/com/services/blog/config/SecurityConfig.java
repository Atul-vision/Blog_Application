package com.services.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.services.blog.security.CustomUserDetailService;
import com.services.blog.security.JwtAuthenticationEntryPoint;
import com.services.blog.security.JwtAuthenticationFilter;

@Configurable
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
    private JwtAuthenticationEntryPoint point;
    @Autowired
    private JwtAuthenticationFilter filter;
    
    @Autowired
    private CustomUserDetailService service;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		
		 http.csrf(csrf -> csrf.disable())
         .cors(cors->cors.disable())
         .authorizeHttpRequests(auth->auth.requestMatchers("/**").
         authenticated().requestMatchers("/api/v1/auth/login").permitAll()
         .anyRequest()
         .authenticated())
         .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
         http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
         http.authenticationProvider(daoAuthenticationProvider());
         return http.build();
		
	}
	
	@Bean
	public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception{
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(this.service);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	

}
