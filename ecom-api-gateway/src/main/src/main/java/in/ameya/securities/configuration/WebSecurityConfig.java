package in.ameya.securities.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) //This enables method level security annotation
@ComponentScan(basePackages = {"in.ameya.securities.interceptor"})
public class WebSecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,AuthenticationManager authenticationManager) throws Exception {
		http
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(request -> request
			.requestMatchers(HttpMethod.GET, "/**").permitAll()
			.requestMatchers(HttpMethod.POST, "/**").permitAll()
			.requestMatchers(HttpMethod.HEAD, "/**").permitAll()
			
			.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			.requestMatchers(HttpMethod.PUT, "/**").permitAll()
			.requestMatchers(HttpMethod.DELETE, "/**").permitAll()
			.requestMatchers(HttpMethod.TRACE, "/**").denyAll()
			
			.requestMatchers("/swagger-ui/**").permitAll()
			.anyRequest()
			.authenticated()
		)
		.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.headers(headers -> 
			headers.xssProtection(
	            xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
	        ).contentSecurityPolicy(
	            cps -> cps.policyDirectives("default-src 'self' 'unsafe-inline'")
	        )
		);
	    
	    return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	
	@Bean
	public WebMvcConfigurer corsMappingConfigurer(@Value("${web.cors.allowedOrigins}") String allowedOrigins) {
	   return new WebMvcConfigurer() {
	       @Override
	       public void addCorsMappings(CorsRegistry registry) {
	           registry.addMapping("/**")
	           .allowedOrigins(allowedOrigins)
	           .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
	           .maxAge(3600);
	       }
	   };
	}
	
}