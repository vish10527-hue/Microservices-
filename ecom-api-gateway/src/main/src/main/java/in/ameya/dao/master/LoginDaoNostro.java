package in.ameya.dao.master;

import java.util.ArrayList;
import java.util.HashMap;

import in.ameya.model.master.LoginVo;
import in.ameya.model.master.MenuVo;

public interface LoginDaoNostro {

	LoginVo getLoginDetails(LoginVo loginVo);

	ArrayList<MenuVo> getMenuList(String userId);
	
	HashMap<String, String> getUserPrivilageList(String userId);

	void logoutUserSession(LoginVo loginVo);

}
