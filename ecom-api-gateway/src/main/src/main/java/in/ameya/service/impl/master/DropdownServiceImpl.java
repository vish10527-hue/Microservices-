package in.ameya.service.impl.master;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.ameya.dao.master.DropdownDao;
import in.ameya.model.master.DropdownVo;
import in.ameya.service.master.DropdownService;

@Service
@Transactional(transactionManager = "MainTransactionManager", rollbackFor=RuntimeException.class)
public class DropdownServiceImpl implements DropdownService {

    @Autowired
    private DropdownDao dropdownDao;

    @Override
    public ArrayList<DropdownVo> getBranchList(String branchDesc, String branchCode) {
        return dropdownDao.getBranchList(branchDesc, branchCode);
    }

    @Override
    public ArrayList<DropdownVo> getLookup(Integer lookupType, String lookupCode, String lookupDesc) {
        return dropdownDao.getLookup(lookupType, lookupCode, lookupDesc);
    }

    @Override
    public ArrayList<DropdownVo> getUserRole(String userRoleCode, String userRoleName) {
        return dropdownDao.getUserRole(userRoleCode, userRoleName);
    }

    @Override
    public ArrayList<DropdownVo> getCurrency(String currCode, String currDesc) {
        return dropdownDao.getCurrency(currCode, currDesc);
    }
    @Override
    public ArrayList<DropdownVo> getMsgType(String msgCode, String msgDesc) {
        return dropdownDao.getMsgType(msgCode, msgDesc);
    }

    @Override
    public ArrayList<DropdownVo> getSystemParams(String paramName) {
        return dropdownDao.getSystemParams(paramName);
    }

}
