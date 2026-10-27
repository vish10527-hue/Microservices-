package in.ameya.service.master;

import java.util.ArrayList;
import java.util.HashMap;

import in.ameya.model.master.LoginVo;
import in.ameya.model.master.MenuVo;

public interface LoginServiceNostro {

	LoginVo getLoginDetails(LoginVo loginVo);
	
	ArrayList<MenuVo> getMenuList(String userId);
	
	HashMap<String, String> getUserPrivilageList(String userId);

	void logoutUserSession(LoginVo loginVo);

}
