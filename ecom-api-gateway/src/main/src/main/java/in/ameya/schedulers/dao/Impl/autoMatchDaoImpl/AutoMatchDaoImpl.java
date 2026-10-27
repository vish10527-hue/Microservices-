package in.ameya.schedulers.dao.Impl.autoMatchDaoImpl;

import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import in.ameya.schedulers.dao.autoMatchDao.AutoMatchDao;
import in.ameya.securities.configuration.JdbcDaoSupport;
import oracle.jdbc.OracleTypes;

@Repository
public class AutoMatchDaoImpl extends JdbcDaoSupport implements AutoMatchDao {

    private static final Logger logger =LogManager.getLogger(AutoMatchDaoImpl.class);

    @Autowired
    @Qualifier("MainDatabase")
    private DataSource dataSource;

    @Override
    public void runAutoMatch(Integer userKey) {

        try {
            SimpleJdbcCall simpleJdbcCall =getMainDBProcedureCall("STP_UPL_PROCESS_AUTO_MATCH");

            simpleJdbcCall.declareParameters(
                    new SqlParameter("IUSER_KEY", OracleTypes.NUMBER)
            );

            simpleJdbcCall.withoutProcedureColumnMetaDataAccess();

            MapSqlParameterSource paramSource = new MapSqlParameterSource()
                            .addValue("IUSER_KEY", userKey);

            simpleJdbcCall.execute(paramSource);

            logger.info("STP_UPL_PROCESS_AUTO_MATCH executed successfully for userKey={}", userKey);

        } catch (Exception e) {
            logger.error("Error executing STP_UPL_PROCESS_AUTO_MATCH", e);
        }
    }
}
