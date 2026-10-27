package in.ameya.securities.interceptor;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import in.ameya.securities.service.EncryptionService;
import in.ameya.util.DecryptionRequestWrapper;
import in.ameya.util.ResponseWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class EncryptorFilter extends GenericFilterBean {
	
	@Autowired
	EncryptionService encryptionService;
	
	Logger logger = LogManager.getLogger();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String url = ((HttpServletRequest) request).getRequestURI();
		if(url.contains("/swagger-ui") || url.contains("/api-docs") || url.contains("/api/lam")) {
			chain.doFilter(request, response);
			return;
		}
		
		DecryptionRequestWrapper wrappedRequest = new DecryptionRequestWrapper((HttpServletRequest) request, encryptionService);
		ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);
		chain.doFilter(wrappedRequest, responseWrapper);
		String responseContent = new String(responseWrapper.getDataStream());
		try {
			responseWrapper.getWriter().write(
					encryptionService.encrypt (responseContent)
					);
			responseWrapper.setContentType(MediaType.TEXT_PLAIN_VALUE);
		} catch (Exception e) {
			logger.error("doFilter()", e);
			responseWrapper.getWriter().write(responseContent);
		}
	}

}
