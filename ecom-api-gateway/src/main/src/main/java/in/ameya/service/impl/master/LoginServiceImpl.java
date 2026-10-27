package in.ameya.service.impl.master;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.LoginDao;
import in.ameya.model.LoginDetails;
import in.ameya.model.Menu;
import in.ameya.service.master.LoginService;

@Service
@Transactional(transactionManager = "MainTransactionManager", rollbackFor=RuntimeException.class)
public class LoginServiceImpl implements LoginService {

	@Autowired
	LoginDao loginDao;
	
	@Override
	public Integer checkUsername(String username) {
		return loginDao.checkUsername(username);
	}

	@Override
	public Map<String, Object> updateAttempts(String username) {
		return loginDao.updateAttempts(username);
	}

	@Override
	public Map<String, Object> updateStatus(Integer userId, int status, String token) {
		return loginDao.updateStatus(userId, status, token);
	}

	@Override
	public Integer fetchLoginDetails(LoginDetails user) {
		return loginDao.fetchLoginDetails(user);
	}

	@Override
	public ArrayList<Menu> getMenus(Integer userId) {
		return loginDao.getMenus(userId);
	}
}
