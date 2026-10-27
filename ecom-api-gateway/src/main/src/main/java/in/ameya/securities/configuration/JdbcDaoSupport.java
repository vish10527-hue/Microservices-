package in.ameya.securities.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

@Component
public class JdbcDaoSupport {

    @Autowired
    @Qualifier("MainDatabase")
    protected DataSource mainDatasource;

//    @Autowired
//    @Qualifier("SwiftBODatabase")
//    protected DataSource swiftBoDatasource;

    @Autowired
    @Qualifier("MainDatabaseTemplate")
    protected JdbcTemplate jdbcTemplateMain;

//    @Autowired
//    @Qualifier("SwiftBODatabaseTemplate")
//    protected JdbcTemplate jdbcTemplateSwiftBO;

    public SimpleJdbcCall getMainDBProcedureCall(String procedureName) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(mainDatasource);
        simpleJdbcCall.withProcedureName(procedureName);
        simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
        return simpleJdbcCall;
    }

//    public SimpleJdbcCall getSwiftBODBProcedureCall(String procedureName) {
//        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(swiftBoDatasource);
//        simpleJdbcCall.withProcedureName(procedureName);
//        simpleJdbcCall.withoutProcedureColumnMetaDataAccess();
//        return simpleJdbcCall;
//    }

    public void setMainDataSource(DataSource dataSource) {
        this.jdbcTemplateMain = new JdbcTemplate(dataSource);
    }

//    public void setSwiftBODataSource(DataSource dataSource) {
//        this.jdbcTemplateSwiftBO = new JdbcTemplate(dataSource);
//    }
}

