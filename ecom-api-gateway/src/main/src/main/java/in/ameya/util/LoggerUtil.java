package in.ameya.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class LoggerUtil {
	
	private static Logger logger = null; 
	
	public LoggerUtil() {
		System.setProperty("log4j.configurationFile","/log4j2.xml");
		logger=LogManager.getLogger(getClass());
	}
	
	public LoggerUtil(Class<?> clazz) {;
		System.setProperty("log4j.configurationFile","/log4j2.xml");
		logger=LogManager.getLogger(clazz);
    }
	
	
	public Logger getLogger(Class clazz) {
		if (clazz == null) {
			throw new NullPointerException("parameter clazz must not be null");
	    }
	    return LogManager.getLogger(clazz.getName());
	}
	
	public void logError(Object obj) {
		logger.error(obj);
	}
	
	
	public void logError(String className, Object obj, Throwable throwable, String userId) {
		logger.error(throwable);		
	}
	
	public void logErrorInsert( Object obj, Throwable throwable, String className, String userId) {
		logInfo(className);
		logger.error(obj, throwable);
	}

	public void logError(Object obj, Throwable throwable) {
		logger.error(obj, throwable);
	}
	
	public void logDebug(Object obj) {
		logger.debug(obj);
	}

	public void logDebug(Object obj, Throwable throwable) {
		logger.debug(obj, throwable);
	}

	public void logInfo(Object obj) {
		logger.info(obj);
	}

	public void logInfo(Object obj, Throwable throwable) {
		logger.info(obj, throwable);
	}
	
	public void setClass(Class<?> class1) {
		logger = getLogger(class1);
	}

}
