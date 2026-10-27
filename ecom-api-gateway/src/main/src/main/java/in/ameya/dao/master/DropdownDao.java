package in.ameya.dao.master;

import java.util.ArrayList;

import in.ameya.model.master.DropdownVo;

public interface DropdownDao {

	ArrayList<DropdownVo> getBranchList(String branchDesc,String branchCode);

	ArrayList<DropdownVo> getLookup(Integer lookupType, String lookupCode, String lookupDesc);

	ArrayList<DropdownVo> getUserRole(String userRoleCode, String userRoleName);

	ArrayList<DropdownVo> getCurrency(String currCode, String currDesc);
	
	ArrayList<DropdownVo> getMsgType(String msgCode, String msgDesc);

	ArrayList<DropdownVo> getSystemParams(String paramName);

}
