package com.param.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.param.filter.JwtTokenFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return super.userDetailsServiceBean();
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/resources/assets/js/**","/adt/organizationMasterList/**","/adt/getUnitByCredentials","/ldapUserCredentialCheck").permitAll() // Public endpoints
				.antMatchers(HttpMethod.POST, "/adt/userLogin").permitAll() // Login endpoint
				.anyRequest().authenticated() // All other requests require authentication
				.and()
				.addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // Custom JWT filter
	}
}
