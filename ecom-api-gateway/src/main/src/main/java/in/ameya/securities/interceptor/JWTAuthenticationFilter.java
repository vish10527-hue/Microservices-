package in.ameya.securities.interceptor;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import in.ameya.securities.service.EncryptionService;
import in.ameya.securities.service.MyUserDetailsService;
import in.ameya.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends GenericFilterBean {
	
	private static final Logger logger = LogManager.getLogger(JWTAuthenticationFilter.class);
	
	@Autowired
	Environment env;
	
	@Autowired
    private JWTUtil jwtUtil;
	
	@Autowired
    private MyUserDetailsService userDetailsService;
	
	@Autowired
	EncryptionService encryptionService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		boolean jwtflag = false;
		
		try {
			final String authorizationHeader = ((HttpServletRequest) request).getHeader("Authorization");
			//final String ipAddress = in.ameya.util.AmeyaUtil.getClientIpAddress((HttpServletRequest) request);
	        String loginId = null;
	        String jwt = null;
	        String url = ((HttpServletRequest) request).getRequestURI();
		    if(!url.contains("/session/authenticate") && !url.contains("/swagger") && !url.contains("/api-docs")
	    		&& !url.contains("/api/lam") && !url.contains("/api/test")
	    		&& !((HttpServletRequest) request).getMethod().equalsIgnoreCase("options")) {						            
	            try
	            {
	            	jwt = authorizationHeader.substring(7);
	            	loginId = jwtUtil.extractUsername(jwt);
	            	if (loginId != null) 
			        {
			            UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);
			
			            if (jwtUtil.validateToken(jwt, userDetails)) {
			            	UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(),userDetails.getAuthorities());
			            	SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			            	
			            	//jwtflag = true;
			            	jwtflag = jwtUtil.checkActive(Integer.parseInt(loginId), jwt);
			            	request.setAttribute("userId", loginId);
			            	
			            	/*
				            if(!jwtUtil.validateToken(jwt, userDetails, ipAddress)) {
			            		loggerUtil.logInfo("JWTAuthenticationFilter.doFilter("+ loginId +"): IP changed to ->" + ipAddress);
			            	}*/
			            }
			            
			            //check jwt token of same user
			            /*LoginVo loginVo = (LoginVo) session.getAttribute("loginVo");
			            if(!loginVo.getLoginId().equalsIgnoreCase(loginId) || !loginVo.getToken().equalsIgnoreCase(jwt))
			            {
			            	jwtflag = false;
			            }*/
			        }
	            }
	            catch (Exception e)
	            {
	            	jwtflag = false;
	            	logger.error("JWTAuthenticationFilter: " + authorizationHeader, e);
				}
	        } else { 
				jwtflag = true; 
			}
			 
		}
		catch(Exception e)
		{
			jwtflag = false;
			logger.error("JWTAuthenticationFilter", e);
		}
        
	    if(jwtflag){
	    	chain.doFilter(request, response);
	    } else {
	    	 ((HttpServletResponse) response).setStatus(HttpStatus.FORBIDDEN.value());
	    	 ((HttpServletResponse) response).addHeader("Access-Control-Allow-Origin", "*");
	    	 response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	    	 try {
				response.getWriter().append(encryptionService.encrypt ("Invalid Token."));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
}