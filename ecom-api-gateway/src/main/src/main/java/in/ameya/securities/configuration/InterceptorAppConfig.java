package in.ameya.securities.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import in.ameya.securities.interceptor.EncryptorFilter;
import in.ameya.securities.interceptor.JWTAuthenticationFilter;


@Configuration
public class InterceptorAppConfig implements WebMvcConfigurer {
	   
	
	  @Autowired JWTAuthenticationFilter authenticateInterceptor;
	  @Autowired EncryptorFilter encInterceptor;
	  
	  @Bean
	  public FilterRegistrationBean<JWTAuthenticationFilter> jwtFilterRegistration() {
		  FilterRegistrationBean<JWTAuthenticationFilter> registration = new FilterRegistrationBean<JWTAuthenticationFilter>();
		  registration.setFilter(authenticateInterceptor);
		  registration.addUrlPatterns("/*");
	      return registration;
	  }
	  
	  @Bean
	  public FilterRegistrationBean<EncryptorFilter> encFilterRegistration() {
		  FilterRegistrationBean<EncryptorFilter> registration = new FilterRegistrationBean<EncryptorFilter>();
		  registration.setFilter(encInterceptor);
		  registration.addUrlPatterns("/*");
	      return registration;
	  }
	  
	 
}