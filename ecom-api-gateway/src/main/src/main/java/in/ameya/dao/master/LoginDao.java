package in.ameya.dao.master;

import java.util.ArrayList;
import java.util.Map;

import in.ameya.model.LoginDetails;
import in.ameya.model.Menu;
public interface LoginDao {

	int checkUsername(String username);

	Map<String, Object> updateAttempts(String username);

	Map<String, Object> updateStatus(Integer userId, int status, String token);

	Integer fetchLoginDetails(LoginDetails user);

	ArrayList<Menu> getMenus(Integer userId);
}
