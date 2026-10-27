package in.ameya.controller.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.IOUtils;
import org.apache.logging.log4j.core.util.datetime.FixedDateFormat.FixedFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import in.ameya.RELIERMainApplication;
import in.ameya.model.CommonDataException;
import in.ameya.model.DBResponse;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	Environment env;
	
	private static final Logger logger = LogManager.getLogger(RELIERMainApplication.class);

	SimpleDateFormat timeFormatter = new SimpleDateFormat(FixedFormat.DEFAULT.getPattern());
	
	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
	public ResponseEntity<HashMap<String, String>> handleApplicationException(HttpServletRequest req, Exception ex) {
		HashMap<String, String> resp = new HashMap<String, String>();
		
		logger.error("Request: " + req.getRequestURL() + " raised " + ex);
		Date raisedOn = new Date();
		try {
			logger.error(IOUtils.toString(req.getReader()), ex);
			if(ex.getCause() != null) {
				resp.put("cause", ex.getCause().toString());
			} else {
				resp.put("cause", ex.getMessage());
			}
		} catch (Exception e) {;}
		
		resp.put("timestamp", timeFormatter.format(raisedOn) + "J");
		resp.put("message", env.getProperty("error.common.msg", "An Error Occured"));
		return new ResponseEntity<HashMap<String, String>>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}