package in.ameya.securities.configuration;

import java.beans.PropertyVetoException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import in.ameya.securities.passwordutility.AsciiEncryption;
import in.ameya.securities.passwordutility.DecryptionNotSupportedException;
import jakarta.annotation.PreDestroy;

@Configuration
@PropertySource(value = "classpath:application.properties")
@EnableTransactionManagement
public class PersistenceConfiguration {

	@Autowired
	public Environment env;
	
	@Autowired
	AsciiEncryption asciiEncryption;
	
	private ComboPooledDataSource mainDataSource = null;
			
	
	/**
	 * 
	 * @return
	 * @throws PropertyVetoException
	 * @throws DecryptionNotSupportedException
	 * @throws SQLException
	 * This method is to get data source by database url, username, password and jdbc driver
	 */
	@Bean(name = "MainDatabase")
	@Primary
	public DataSource getmainDataSource() throws PropertyVetoException,  SQLException, DecryptionNotSupportedException {
		mainDataSource = new ComboPooledDataSource();
	
		mainDataSource.setDriverClass(env.getProperty("spring.datasource.main.driver-class-name"));
		mainDataSource.setJdbcUrl(env.getProperty("spring.datasource.main.url"));
		mainDataSource.setUser(env.getProperty("spring.datasource.main.username"));	
		mainDataSource.setPassword(asciiEncryption.decrypt(env.getProperty("spring.datasource.main.password")));	

		mainDataSource.setMaxPoolSize(env.getProperty("c3p0.maxPoolSize", Integer.class, 10));
		mainDataSource.setInitialPoolSize(env.getProperty("c3p0.initialPoolSize", Integer.class, 10));
		mainDataSource.setMinPoolSize(env.getProperty("c3p0.minPoolSize", Integer.class, 5));
		mainDataSource.setMaxStatements(env.getProperty("c3p0.maxStatements", Integer.class, 10));

		mainDataSource.setPreferredTestQuery(env.getProperty("spring.datasource.main.test.sql"));
		mainDataSource.setTestConnectionOnCheckout(env.getProperty("c3p0.testConnectionOnCheckout", Boolean.class, false));
		mainDataSource.setTestConnectionOnCheckin(env.getProperty("c3p0.testConnectionOnCheckin", Boolean.class, true));
		mainDataSource.setMaxIdleTime(env.getProperty("c3p0.maxIdleTime", Integer.class, 1500));
		mainDataSource.setIdleConnectionTestPeriod(env.getProperty("c3p0.idleConnectionTestPeriod", Integer.class, 1000));  
		mainDataSource.setAcquireIncrement(env.getProperty("c3p0.acquireIncrement", Integer.class, 1));
		mainDataSource.setAcquireRetryAttempts(env.getProperty("c3p0.acquireRetryAttempts", Integer.class, 1));
		mainDataSource.setBreakAfterAcquireFailure(env.getProperty("c3p0.breakAfterAcquireFailure", Boolean.class, false));

		return mainDataSource;		
	}
	
	@PreDestroy
	public void cleanUp() throws Exception {
	    if (mainDataSource != null) mainDataSource.close();
	}
	
	
	/**
	 * 
	 * @param dataSource
	 * @return
	 * This method is to get NamedParameterJdbcTemplate to execute SQL statement and extract results
	 *  by specifying the named parameters instead of classic placeholder('?') argument
	 */
	@Bean(name = "NamedParameterMain")
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplateMain(@Qualifier("MainDatabase") DataSource dataSource) {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		return namedParameterJdbcTemplate;
	}
	
	
	/**
	 * 
	 * @param dataSource
	 * @return
	 * This method is to get JdbcTemplate to execute SQL statement and extract results
	 * by specifying classic placeholder('?') argument
	 */
	@Bean(name = "MainDatabaseTemplate")
    public JdbcTemplate getJdbcTemplateMain(@Qualifier("MainDatabase") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setResultsMapCaseInsensitive(true);
        return jdbcTemplate;
    }
	
	@Bean(name = "MainDatabaseJdbcCall")
    public SimpleJdbcCall getSimpleJdbcCallMain(@Qualifier("MainDatabase") DataSource dataSource) {
		SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dataSource);
		simpleJdbcCall.setReturnValueRequired(true);
		//simpleJdbcCall.withCatalogName(env.getProperty("database.schema.name"));
        return simpleJdbcCall;
    }
	
	/**
	 * 
	 * @param dataSource
	 * @return
	 * This method is to get TransactionManager which handles 
	 * transactional activities - running a query, wrapped in a transaction etc
	 */
	@Bean("MainTransactionManager")
	@Primary
	public PlatformTransactionManager getPlatformTransactionManagerMain(@Qualifier("MainDatabase") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
		
}
