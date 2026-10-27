package in.ameya.securities.service;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import in.ameya.util.AmeyaUtil;

@Component
public class JwtAuthentocationEntry implements AuthenticationEntryPoint {

	@Override
	public void commence(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, AuthenticationException authException)
			throws IOException, jakarta.servlet.ServletException {
		AmeyaUtil.showError(response, "Invalid JWT Token.");		
	}
	
}
