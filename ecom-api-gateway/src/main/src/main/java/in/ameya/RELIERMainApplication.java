package in.ameya;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.module.SimpleModule;

import in.ameya.util.UppercaseStringDeserializer;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class RELIERMainApplication extends SpringBootServletInitializer {

    private static final Logger log = LoggerFactory.getLogger(RELIERMainApplication.class);
	
	public static void main(String[] args) {
		try {
			SpringApplication.run(RELIERMainApplication.class, args);
		} catch (Exception e) {
			if(e.getClass().getName().contains("SilentExitException")) {
				System.err.println("Spring is restarting the main thread - See spring-boot-devtools");
	        } else {
	            throw e;
	        }
		}
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(RELIERMainApplication.class);
	}	
	
    @Bean
    public RestTemplate getRestTemplate(@Autowired Environment env) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        Proxy proxy = Proxy.NO_PROXY;
		if(!env.getProperty("http.proxy.host", "").equalsIgnoreCase("")) {
			proxy = new Proxy(
					Proxy.Type.HTTP, 
					new InetSocketAddress(
							env.getProperty("http.proxy.host", String.class),
							env.getProperty("http.proxy.port", Integer.class, 80)
							)
					);
			log.info("Using Proxy: http:/" + proxy.address());
		} else {
			log.error("Not Using Proxy !!!");
		}
		
		
        requestFactory.setProxy(proxy);
        return new RestTemplate(requestFactory);
    }
    @Bean
    @Primary
    public ObjectMapper getObjectMapper() {
    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    	objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    	
    	SimpleModule uppercaseModule = new SimpleModule();
    	uppercaseModule.addDeserializer(String.class, new UppercaseStringDeserializer());
    	objectMapper.registerModule(uppercaseModule);
    	
    	return objectMapper;
    }
}