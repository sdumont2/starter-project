package com.dumontsean.data.support.entitymanager;

import org.hibernate.ogm.jpa.HibernateOgmPersistence;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.spi.PersistenceProvider;

public class SpringHibernateJpaVendorAdaptor extends HibernateJpaVendorAdapter implements EnvironmentAware {

    private Environment environment;

    @NonNull
    @Override
    public PersistenceProvider getPersistenceProvider() {
        if(environment != null) {
            String providerType = environment.getProperty("starter.db.type", "ogm-mongodb");
            if (providerType.toLowerCase().startsWith("ogm")) {
                return new HibernateOgmPersistence();
            }
        }
        return super.getPersistenceProvider();
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }
}
