package com.viettelpost.api.base;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
public class DbCustomerCfg {
    @Bean(name = "customerSource", destroyMethod = "close")
    public DataSource getDataSource() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();

        dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://trungdq306.chxr0uvxxx0v.us-east-1.rds.amazonaws.com:3306/chatbot");
        dataSource.setUser("admin");
        dataSource.setPassword("Taolatrung1!");
        dataSource.setMinPoolSize(1);
        dataSource.setMaxPoolSize(10);
        dataSource.setMaxIdleTime(500);
        dataSource.setMaxIdleTimeExcessConnections(300);
        dataSource.setIdleConnectionTestPeriod(250);
        dataSource.setTestConnectionOnCheckout(true);
        dataSource.setTestConnectionOnCheckin(true);
        dataSource.setPreferredTestQuery("SELECT 1 FROM DUAL");
        dataSource.setAcquireRetryDelay(2000);
        dataSource.setAcquireRetryAttempts(999);
        dataSource.setAcquireIncrement(1);
        System.out.println("## getDataSource: " + dataSource);
        return dataSource;
    }

    @Autowired
    @Bean(name = "customerFactory")
    @Primary
    public SessionFactory getSessionFactory(@Qualifier("customerSource") DataSource dataSource) throws Exception {
        Properties properties = new Properties();

        // application.properties
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.show_sql", "false");
        properties.put("current_session_context_class", "org.springframework.orm.hibernate5.SpringSessionContext");

        // org.postgresql.jdbc.PgConnection.createClob() is not yet implemented.
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", false);

        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

        // factoryBean.setPackagesToScan(new String[]{""});
        factoryBean.setDataSource(dataSource);
        factoryBean.setHibernateProperties(properties);
        factoryBean.afterPropertiesSet();
        SessionFactory sf = factoryBean.getObject();
        System.out.println("## getSessionFactory: " + sf);
        return sf;
    }

    @Autowired
    @Bean(name = "customerTransactionManager")
    @Primary
    public HibernateTransactionManager getTransactionManager(@Qualifier("customerFactory") SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);

        return transactionManager;
    }
}
