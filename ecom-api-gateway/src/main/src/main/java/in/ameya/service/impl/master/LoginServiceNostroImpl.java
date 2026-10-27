package in.ameya.service.impl.master;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.LoginDaoNostro;
import in.ameya.model.master.LoginVo;
import in.ameya.model.master.MenuVo;
import in.ameya.service.master.LoginServiceNostro;

@Transactional(rollbackFor=RuntimeException.class)
@Service
public class LoginServiceNostroImpl implements LoginServiceNostro{

	@Autowired
	LoginDaoNostro loginDao;
	
	@Override
	public LoginVo getLoginDetails(LoginVo loginVo) {		
		return loginDao.getLoginDetails(loginVo);
	}

	@Override
	public ArrayList<MenuVo> getMenuList(String userId) {
		return loginDao.getMenuList(userId);
	}

	@Override
	public HashMap<String, String> getUserPrivilageList(String userId) {
		return loginDao.getUserPrivilageList(userId);
	}

	@Override
	public void logoutUserSession(LoginVo loginVo) {
		loginDao.logoutUserSession(loginVo);		
	}

}
