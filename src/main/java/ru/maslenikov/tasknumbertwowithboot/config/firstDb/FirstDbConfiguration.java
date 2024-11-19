package ru.maslenikov.tasknumbertwowithboot.config.firstDb;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import ru.maslenikov.tasknumbertwowithboot.config.HibernateProperty;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = FirstDbConfiguration.ENTITY_MANAGER_FACTORY,
                       transactionManagerRef = FirstDbConfiguration.TRANSACTION_MANAGER,
                       basePackages = FirstDbConfiguration.JPA_REPOSITORY_PACKAGE)
@PropertySource("classpath:firstDb.properties")
public class FirstDbConfiguration {

    protected static final String PROPERTY_PREFIX = "db";
    protected static final String JPA_REPOSITORY_PACKAGE = "ru.maslenikov.tasknumbertwowithboot.repository.firstDb";
    protected static final String ENTITY_PACKAGE = "ru.maslenikov.tasknumbertwowithboot.model.firstDb";
    protected static final String ENTITY_MANAGER_FACTORY = "firstDbEntityManagerFactory";
    protected static final String DATA_SOURCE = "firstDbDataSource";
    private static final String DATABASE_PROPERTY = "firstDbDatabaseProperty";
    protected static final String TRANSACTION_MANAGER = "firstDbTransactionManager";

    private final HibernateProperty hibernateProperty;

    public FirstDbConfiguration(HibernateProperty hibernateProperty) {
        this.hibernateProperty = hibernateProperty;
    }

    /*
    // если бы мы вынесли dataSourceProperty в отдельный файл
    @Bean(DATABASE_PROPERTY)
    @ConfigurationProperties(PROPERTY_PREFIX)
    public FirstDbDataSource firstDbDataSourceProperty() {
        return new FirstDbDataSource();
    }

    @Bean(DATA_SOURCE)
    public DataSource firstDbDataSource(@Qualifier(DATABASE_PROPERTY) FirstDbDataSource databaseProperty) {
        return DataSourceBuilder.create()
                .username(databaseProperty.getUsername())
                .password(databaseProperty.getPassword())
                .url(databaseProperty.getUrl())
                .driverClassName(databaseProperty.getClassDriver()).build();
    }
    */

    @Bean(DATA_SOURCE)
    @ConfigurationProperties(PROPERTY_PREFIX)
    public DataSource firstDbDataSource(@Value("${db.url}") String url) {
        return DataSourceBuilder.create().url(url).build();
    }

    @Bean(ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean db1EntityManagerFactory(@Qualifier(DATA_SOURCE) DataSource dataSource) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(ENTITY_PACKAGE); // больше в конфигурацию в ручную сущности не добавляем, а берем их этой папки
        em.setPersistenceUnitName(ENTITY_MANAGER_FACTORY);

        Properties properties = new Properties();
        properties.put("hibernate.dialect", hibernateProperty.getDialect());
        properties.put("hibernate.show_sql", hibernateProperty.getShow_sql());
        em.setJpaProperties(properties);

        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }

    @Bean(TRANSACTION_MANAGER)
    public PlatformTransactionManager sqlSessionTemplate(
            @Qualifier(ENTITY_MANAGER_FACTORY) LocalContainerEntityManagerFactoryBean entityManager,
            @Qualifier(DATA_SOURCE) DataSource dataSource
    ) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager.getObject());
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

}
