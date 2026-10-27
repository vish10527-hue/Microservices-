package in.ameya.dao.impl.reports;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import in.ameya.dao.reports.MsgReportDao;
import in.ameya.model.DBResponse;
import in.ameya.model.reports.NostroSummaryVo;
import in.ameya.securities.configuration.JdbcDaoSupport;
import in.ameya.util.DBConstants;
import oracle.jdbc.OracleTypes;

@Repository
public class MsgReportDaoImpl extends JdbcDaoSupport implements MsgReportDao {

	private static final Logger logger = LogManager.getLogger(MsgReportDaoImpl.class);

	@Autowired
	@Qualifier("MainDatabase")
	private DataSource dataSource;

	@Override
	public DBResponse getNostroSummaryList(String fromDate, String toDate, Integer loginUserKey) {

		try {
			SimpleJdbcCall jdbcCall = getMainDBProcedureCall(
					DBConstants.PKG_TRANSACTION_TWO_STP_FETCH_NOSTRO_SUMMERY_LIST);

			jdbcCall.withoutProcedureColumnMetaDataAccess().declareParameters(
					new SqlParameter("IFROM_DATE", OracleTypes.VARCHAR),
					new SqlParameter("ITO_DATE", OracleTypes.VARCHAR),
					new SqlParameter("ILOGIN_USER_KEY", OracleTypes.NUMBER),
					new SqlOutParameter("O_CURSOR", OracleTypes.CURSOR, new NostroSummaryRowMapper()));

			MapSqlParameterSource params = new MapSqlParameterSource().addValue("IFROM_DATE", fromDate)
					.addValue("ITO_DATE", toDate).addValue("ILOGIN_USER_KEY", loginUserKey);

			Map<String, Object> out = jdbcCall.execute(params);

			@SuppressWarnings("unchecked")
			List<NostroSummaryVo> list = (List<NostroSummaryVo>) out.get("O_CURSOR");

			return new DBResponse(1, "Success", list);

		} catch (Exception e) {
			logger.error("Error fetching Nostro Summary List", e);
			return new DBResponse(-1, "Error fetching Nostro Summary List", null);
		}
	}

	// ================= ROW MAPPER =================
	private static class NostroSummaryRowMapper implements RowMapper<NostroSummaryVo> {

		@Override
		public NostroSummaryVo mapRow(ResultSet rs, int rowNum) throws SQLException {

			NostroSummaryVo n = new NostroSummaryVo();

			n.setNostroName(rs.getString("NOSTRO_NAME"));
			n.setAccountId(rs.getString("ACCOUNT_ID"));
			n.setCurrCode(rs.getString("CURR_CODE"));
			n.setOpenAmt(rs.getBigDecimal("OPEN_AMT"));
			n.setCreditSum(rs.getBigDecimal("CREDIT_SUM"));
			n.setCreditCnt(rs.getInt("CREDIT_CNT"));
			n.setDebitSum(rs.getBigDecimal("DEBIT_SUM"));
			n.setDebitCnt(rs.getInt("DEBIT_CNT"));
			n.setDebitCreditSum(rs.getBigDecimal("DEBIT_CREDIT_SUM"));
			n.setCloseAmt(rs.getBigDecimal("CLOSE_AMT"));
			n.setBalDiffSum(rs.getBigDecimal("BAL_DIFF_SUM"));
			n.setUnconfirmDebitCnt(rs.getInt("UNCONFIRM_DEBIT_CNT"));
			n.setUnconfirmDebitSum(rs.getBigDecimal("UNCONFIRM_DEBIT_SUM"));
			n.setUnconfirmCreditCnt(rs.getInt("UNCONFIRM_CREDIT_CNT"));
			n.setUnconfirmCreditSum(rs.getBigDecimal("UNCONFIRM_CREDIT_SUM"));

			return n;
		}
	}
}
