package me.kidult.Config.C3P0;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;

/**
 * Created by minisv on 2017. 4. 20..
 */
@Configuration
@ConfigurationProperties(prefix = "c3p0")
public class C3P0Config {

  private int maxPoolSize;
  private int minPoolSize;
  private int initialPoolSize;

  private int acquireIncrement;
  private int acquireRetryAttempts;
  private int acquireRetryDelay;

  private int idleConnectionTestPeriod;
  private String preferredTestQuery;
  private boolean testConnectionOnCheckin;
  private boolean testConnectionOnCheckout;

  private int maxStatements;
  private int maxIdleTime;

  private String url;
  private String username;
  private String password;
  private String driverClassName;

  @Bean
  public ComboPooledDataSource dataSource() throws PropertyVetoException {

    ComboPooledDataSource dataSource = new ComboPooledDataSource();

    dataSource.setMaxPoolSize(maxPoolSize);
    dataSource.setMinPoolSize(minPoolSize);
    dataSource.setInitialPoolSize(initialPoolSize);

    dataSource.setAcquireIncrement(acquireIncrement);
    dataSource.setAcquireRetryAttempts(acquireRetryAttempts);
    dataSource.setAcquireRetryDelay(acquireRetryDelay);

    dataSource.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
    dataSource.setPreferredTestQuery(preferredTestQuery);
    dataSource.setTestConnectionOnCheckin(testConnectionOnCheckin);
    dataSource.setTestConnectionOnCheckout(testConnectionOnCheckout);

    dataSource.setMaxStatements(maxStatements);
    dataSource.setMaxIdleTime(maxIdleTime);

    dataSource.setJdbcUrl(url);
    dataSource.setUser(username);
    dataSource.setPassword(password);
    dataSource.setDriverClass(driverClassName);

    return dataSource;
  }

}
