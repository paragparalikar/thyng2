package com.thyng.web.security;

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
import com.thyng.domain.user.User;
import com.thyng.domain.user.UserDTO;
import com.thyng.domain.user.UserMapper;
import com.thyng.domain.user.UserService;

@Configuration
@EnableWebSecurity 
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{

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
		.userDetailsService(email -> new ThyngUserDetails(userService.findByEmail(email)))
		.passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/public/**/*").permitAll()
		.anyRequest().authenticated().and().httpBasic().and()
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
