package com.thyng.aspect.web;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

@EnableWebMvc
@Configuration
public class WebConfiguration implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/public/**/*").addResourceLocations("/public/");
	}
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/list-tenants").setViewName("pages/tenants/list");
        registry.addViewController("/view-tenant").setViewName("pages/tenants/view");
        registry.addViewController("/edit-tenant").setViewName("pages/tenants/edit");
        registry.addViewController("/list-gateways").setViewName("pages/gateways/list");
        registry.addViewController("/view-gateway").setViewName("pages/gateways/view");
        registry.addViewController("/edit-gateway").setViewName("pages/gateways/edit");
        
        registry.addViewController("/list-things").setViewName("pages/things/list");
        registry.addViewController("/view-thing").setViewName("pages/things/view");
        registry.addViewController("/edit-thing").setViewName("pages/things/edit");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/public/", ".jsp");
	}
	
	@Bean
	public ObjectMapper objectMapper(){
		return new ObjectMapper();
	}
	
	@Bean
	public FilterRegistrationBean<RequestContextFilter> requestContextFilter(){
		return new FilterRegistrationBean<>(new RequestContextFilter());
	}
	
}
