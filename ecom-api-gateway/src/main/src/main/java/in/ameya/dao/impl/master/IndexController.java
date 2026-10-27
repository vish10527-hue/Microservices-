package in.ameya.dao.impl.master;

import java.security.PrivateKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import in.ameya.model.master.LoginVo;
import in.ameya.securities.passwordutility.AsymmetricEncrypter;
import in.ameya.util.LoggerUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {
	
	@Autowired
	private LoggerUtil loggerUtil;
	
	@RequestMapping("/") // for index page
	public String home(ModelMap map, HttpSession session, HttpServletRequest request) {
		LoginVo loginVo = new LoginVo();
		//System.out.println("Inside Index Controller");
		loginVo = setEncryptedPasswordSession(session, loginVo);
		map.addAttribute("loginVo", loginVo); 	
		//System.out.println("session id before"+session.getId());	
		/*return "common/error";*/
		return "common/login";
		
	}
	
	
	public LoginVo setEncryptedPasswordSession(HttpSession session, LoginVo loginVo)
	{
		try
		{
			AsymmetricEncrypter encrypter = new  AsymmetricEncrypter("RSA");
			encrypter.generateKeyPair(2048);
			byte[] encodedBytes = encrypter.getPublicKey().getEncoded();
			//String pubKey = (new sun.misc.BASE64Encoder()).encode(encodedBytes);
			String pubKey = java.util.Base64.getEncoder().encodeToString(encodedBytes);
			PrivateKey privateKey  = encrypter.getPrivateKey();	
			session.setAttribute("encrypter",encrypter);
			session.setAttribute("privatekey",privateKey); 
			loginVo.setPublickey(pubKey);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "IndexController-setEncryptedPasswordSession", "");
		}
		return loginVo;
	}
}
