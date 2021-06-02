package com.dumontsean.starterwebapp.config;

import com.dumontsean.data.support.entitymanager.SpringHibernateJpaVendorAdaptor;
import com.dumontsean.data.support.entitymanager.SpringLocalContainerEntityManagerFactoryBean;
import com.dumontsean.data.support.repository.NoCriteriaJpaRepositoryFactoryBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.dumontsean",
        repositoryFactoryBeanClass = NoCriteriaJpaRepositoryFactoryBean.class)
public class ApplicationConfig {

    private static final Logger log = LogManager.getLogger(ApplicationConfig.class);

    //Hibernate Persistence XML
    @Bean(name = "entityManagerFactory")
    public SpringLocalContainerEntityManagerFactoryBean entityManagerFactory(Environment env) {
        SpringLocalContainerEntityManagerFactoryBean factory = new SpringLocalContainerEntityManagerFactoryBean(env);
        String dbType = env.getProperty("starter.db.type", "ogm-mongodb");
        SpringHibernateJpaVendorAdaptor shjpa = new SpringHibernateJpaVendorAdaptor();
        shjpa.setEnvironment(env);
        factory.setJpaVendorAdapter(shjpa);
        factory.setPersistenceUnitName(dbType);
        return factory;
    }

    @Bean
    @DependsOn("entityManagerFactory")
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor() {
        return new PersistenceAnnotationBeanPostProcessor();
    }

    //Listener

    /*@Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }*/

}
