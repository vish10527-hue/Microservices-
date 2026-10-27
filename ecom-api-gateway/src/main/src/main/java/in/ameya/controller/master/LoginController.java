package in.ameya.controller.master;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import in.ameya.model.LoginDetails;
import in.ameya.model.Menu;
import in.ameya.securities.service.MyUserDetailsService;
import in.ameya.service.master.LoginService;
import in.ameya.util.EmpAuthentication;
import in.ameya.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/session")
@PropertySource(value = "classpath:application.properties")
public class LoginController {

	private static final Logger logger = LogManager.getLogger(LoginController.class);

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	Environment env;

	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private EmpAuthentication authenticator;
	
	@Autowired
	LoginService loginService;
		
	
	ObjectMapper objectMapper = new ObjectMapper();
	LoginController() {
		objectMapper = new ObjectMapper();
    	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    	objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
	}

	@PostMapping("/authenticate")
	public ResponseEntity<HashMap<String, Object>> authUser(HttpServletRequest request) {
	    HashMap<String, Object> resp = new HashMap<String, Object>();
	    LoginDetails user;
	    try {
	        user = objectMapper.readValue(request.getReader(), LoginDetails.class);
	        user.setLoginId(user.getLoginId().toUpperCase());
	    } catch (Exception e) {
	        resp.put("details", e.getLocalizedMessage());
	        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
	    }
	    
	    try {
	        String ipAddress = request.getRemoteAddr();
	        
	        Integer userKey = loginService.checkUsername(user.getLoginId());
	        if (userKey != null && userKey != 0) {
	        	if (user.getGalUserFlag() != null && user.getGalUserFlag() == 1) {
	     	        loginService.updateStatus(userKey, 0, null);
	     	        jwtUtil.setToken(user.getUserKey(), null);
	     	    }
	        	
	            //LDAP
	        	boolean status = false;
	        	String isLdapEnable = env.getProperty("ldap.isEnable"); 
	        	if("1".equalsIgnoreCase(isLdapEnable)) {
	        		Hashtable validate_user  = authenticator.authenticateUser(user.getLoginId(), user.getUserSessionId());	
	        		if(validate_user.containsKey("no-error")){
				    	status = true;				    	
		            } else {
		            	resp.putAll(loginService.updateAttempts(user.getLoginId()));
		                resp.put("status", HttpStatus.UNAUTHORIZED);
		                resp.put("message", env.getProperty("msg.login.error"));
		            	status = false;
		            }
	        	}else {
	        		status = true;
	        	}
	        	
	        	if (status) {
	        		
	        		userKey = loginService.fetchLoginDetails(user);
	        	    if (userKey == null) {
	        	        resp.put("status", HttpStatus.FORBIDDEN);
	        	        resp.put("message", user.getSystemAlert());
	        	        return new ResponseEntity<HashMap<String, Object>>(resp, (HttpStatus) resp.get("status"));
	        	    }

	        	    user.setDeviceIpAddress(ipAddress);
	        	    String deviceInfo = request.getHeader("User-Agent");
	        	    user.setDeviceInfo(deviceInfo);
	        	    user.setUserKey(userKey);
	        	    
        	        final UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(userKey));
        	        final String token = jwtUtil.generateToken(userDetails, ipAddress);
        	        resp.putAll(loginService.updateStatus(userKey, 1, token));
        	        jwtUtil.setToken(user.getUserKey(), token);
        	        resp.put("token", token);
        	        resp.put("status", HttpStatus.OK);
        	        resp.put("user", user);
        	    
	        	}

	           // End of LDAP
	        } else {
	            resp.put("status", HttpStatus.NOT_FOUND);
	            resp.put("message",env.getProperty("msg.login.error"));
	        }
	    } catch (Exception e) {
	        logger.error(e);
	        resp.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    
	    return new ResponseEntity<HashMap<String, Object>>(resp, (HttpStatus) resp.get("status"));
	}
	
	@GetMapping("/menu")
	public ResponseEntity<ArrayList<Menu>> getUserMenu(@RequestAttribute() Integer userId) {
		return new ResponseEntity<ArrayList<Menu>>(loginService.getMenus(userId), HttpStatus.OK);
	}
	
	@PostMapping("/kill")
	public ResponseEntity<HashMap<String, Object>> logout(@RequestAttribute() Integer userId) {
		HashMap<String, Object> resp = (HashMap<String, Object>) loginService.updateStatus(userId, 0, null);
		return new ResponseEntity<HashMap<String, Object>>(resp, HttpStatus.ACCEPTED);
	}
	
}
