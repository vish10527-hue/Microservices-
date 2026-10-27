package in.ameya.dao.impl.master;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import in.ameya.dao.master.DropdownDao;
import in.ameya.model.master.DropdownVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.DBConstants;
import oracle.jdbc.OracleTypes;

@Repository
public class DropdownDaoImpl extends JdbcDaoSupport implements DropdownDao {
	
	@Autowired
	@Qualifier("MainDatabase")
	protected DataSource dataSource;
	
    private class getDropDownValue implements RowMapper<DropdownVo>{
    	
    	private String[] param;
    	
		public getDropDownValue(String... param) {
			this.param = param;
		}

		@Override
		public DropdownVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			DropdownVo dropdownVo = new DropdownVo();
			dropdownVo.setKey(rs.getString(param[0]));
			StringBuilder value = new StringBuilder();
			for(int i=1; i < param.length; i++) {
				value = value.append((value.isEmpty() ? "" : "::") + rs.getString(param[i]));
			}
			dropdownVo.setValue(value.toString());
			return dropdownVo;
		}
    	
    }
	
    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<DropdownVo> getBranchList(String branchCode, String branchDesc) {
        ArrayList<DropdownVo> list = new ArrayList<>();
        try {
            SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.PKGDROPDOWN_STP_SEL_BRANCH_MASTER);

            simpleJdbcCall.declareParameters(
                    new SqlParameter("IBRANCH_CODE", OracleTypes.VARCHAR),
                    new SqlParameter("IBRANCH_DESC", OracleTypes.VARCHAR),
                    new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new getDropDownValue("BRANCH_DESCRIPTION","BRANCH_KEY")));

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("IBRANCH_CODE", branchCode)
                    .addValue("IBRANCH_DESC", branchDesc);

            simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
            Map<String, Object> result = simpleJdbcCall.execute(params);

            if (result != null) {
                list = (ArrayList<DropdownVo>) result.get("IO_CURSOR");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<DropdownVo> getLookup(Integer lookupType, String lookupCode, String lookupDesc) {
        ArrayList<DropdownVo> list = new ArrayList<>();
        try {
            SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.PKGDROPDOWN_STP_SEL_LOOKUP_MASTER);

            simpleJdbcCall.declareParameters(
                    new SqlParameter("ILOOKUP_TYPE", OracleTypes.NUMBER),
                    new SqlParameter("ILOOKUP_CODE", OracleTypes.VARCHAR),
                    new SqlParameter("ILOOKUP_DESC", OracleTypes.VARCHAR),
                    new SqlOutParameter("IO_CURSOR", OracleTypes.CURSOR)
            );

            MapSqlParameterSource params = new MapSqlParameterSource()
            		.addValue("ILOOKUP_TYPE", lookupType)
                    .addValue("ILOOKUP_CODE", lookupCode)
                    .addValue("ILOOKUP_DESC", lookupDesc);

            simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
            Map<String, Object> result = simpleJdbcCall.execute(params);

            if (result != null && result.get("IO_CURSOR") != null) {
                list = (ArrayList<DropdownVo>) result.get("IO_CURSOR");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

	@SuppressWarnings("unchecked")
    @Override
    public ArrayList<DropdownVo> getUserRole(String userRoleCode, String userRoleName) {
        ArrayList<DropdownVo> list = new ArrayList<>();
        try {
            SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.PKGDROPDOWN_STP_SEL_USER_ROLE_MASTER);

            simpleJdbcCall.declareParameters(
                    new SqlParameter("IUSER_ROLE_CODE", OracleTypes.VARCHAR),
                    new SqlParameter("IUSER_ROLE_NAME", OracleTypes.VARCHAR),
                    new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new getDropDownValue("USER_ROLE_KEY","BRANCH_DESCRIPTION")));

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("IUSER_ROLE_CODE", userRoleCode)
                    .addValue("IUSER_ROLE_NAME", userRoleName);

            simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
            Map<String, Object> result = simpleJdbcCall.execute(params);

            if (result != null) {
                list = (ArrayList<DropdownVo>) result.get("IO_CURSOR");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<DropdownVo> getCurrency(String currCode, String currDesc) {
        ArrayList<DropdownVo> list = new ArrayList<>();
        try {
            SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.PKGDROPDOWN_STP_SEL_CURRENCY_MASTER);

            simpleJdbcCall.declareParameters(
                    new SqlParameter("ICURR_CODE", OracleTypes.VARCHAR),
                    new SqlParameter("ICURR_DESC", OracleTypes.VARCHAR),
                    new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new getDropDownValue("CURR_ID","CURR_DESCRIPTION")));

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("ICURR_CODE", currCode)
                    .addValue("ICURR_DESC", currDesc);

            simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
            Map<String, Object> result = simpleJdbcCall.execute(params);

            if (result != null) {
                list = (ArrayList<DropdownVo>) result.get("IO_CURSOR");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<DropdownVo> getMsgType(String msgCode, String msgDesc) {
        ArrayList<DropdownVo> list = new ArrayList<>();
        try {
            SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.PKGDROPDOWN_STP_SEL_MSG_TYPE);

            simpleJdbcCall.declareParameters(
                    new SqlParameter("IMSG_CODE", OracleTypes.VARCHAR),
                    new SqlParameter("IMSG_DES", OracleTypes.VARCHAR),
                    new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new getDropDownValue("MSG_TYPE_KEY","MSG_DESCRIPTION")));

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("IMSG_CODE", msgCode)
                    .addValue("IMSG_DES", msgDesc);

            simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
            Map<String, Object> result = simpleJdbcCall.execute(params);

            if (result != null) {
                list = (ArrayList<DropdownVo>) result.get("IO_CURSOR");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<DropdownVo> getSystemParams(String paramName) {
        ArrayList<DropdownVo> list = new ArrayList<>();
        try {
            SimpleJdbcCall simpleJdbcCall = getMainDBProcedureCall(DBConstants.PKGDROPDOWN_STP_SEL_SYSTEM_PARAMS);

            simpleJdbcCall.declareParameters(
                    new SqlParameter("IPARAM_NAME", OracleTypes.VARCHAR),
                    new SqlOutParameter("IO_CURSOR", OracleTypes.REF_CURSOR, new getDropDownValue("PARAM_VALUE_IN_CHAR","PARAM_VALUE_IN_INT","PARAM_VALUE_IN_DATE")));

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("IPARAM_NAME", paramName);

            simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
            Map<String, Object> result = simpleJdbcCall.execute(params);

            if (result != null) {
                list = (ArrayList<DropdownVo>) result.get("IO_CURSOR");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}