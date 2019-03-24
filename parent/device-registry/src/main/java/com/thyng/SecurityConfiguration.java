package com.thyng;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thyng.model.dto.UserDTO;
import com.thyng.model.entity.User;
import com.thyng.model.mapper.UserMapper;
import com.thyng.service.UserService;

@Configuration
@EnableWebSecurity 
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private final AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.authenticationEventPublisher(authenticationEventPublisher())
		.userDetailsService(userService)
		.passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/public/**/*").permitAll()
		.anyRequest().authenticated().and()
		.formLogin().loginPage("/login").successHandler(this::onAuthenticationSuccess).permitAll().and()
		.logout().permitAll().and()
		.csrf().disable();
	}
	
	private void onAuthenticationSuccess(HttpServletRequest request, 
			HttpServletResponse response, Authentication authentication) throws IOException, ServletException{
		final User user = (User)authentication.getPrincipal();
		final UserDTO dto = userMapper.toDTO(user);
		final HttpSession session = request.getSession();
		session.setAttribute("user", dto);
		session.setAttribute("user-json", objectMapper.writeValueAsString(dto));
		successHandler.onAuthenticationSuccess(request, response, authentication);
	}
	
	@Bean
    public DefaultAuthenticationEventPublisher authenticationEventPublisher() {
        return new DefaultAuthenticationEventPublisher();
    }
		
}
