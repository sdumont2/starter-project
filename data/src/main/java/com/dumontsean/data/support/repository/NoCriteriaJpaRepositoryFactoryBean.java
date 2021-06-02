package com.dumontsean.data.support.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.lang.NonNull;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class NoCriteriaJpaRepositoryFactoryBean<R extends JpaRepository<T, ID>, T, ID extends Serializable>
        extends JpaRepositoryFactoryBean<R, T, ID> {

    /**
     * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public NoCriteriaJpaRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @NonNull
    @Override
    protected RepositoryFactorySupport createRepositoryFactory(@NonNull EntityManager entityManager) {
        return new NoCriteriaJpaRepositoryFactory<T, ID>(entityManager);
    }

    private static class NoCriteriaJpaRepositoryFactory<T, ID extends Serializable> extends JpaRepositoryFactory {

        private EntityManager entityManager;

        public void setEntityManager(EntityManager entityManager) {
            this.entityManager = entityManager;
        }

        /**
         * Simple jpa executor factory constructor
         *
         * @param entityManager entity manager
         */
        public NoCriteriaJpaRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
            this.entityManager = entityManager;
        }

        @NonNull
        @Override
        protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information,
                                                                        @NonNull EntityManager entityManager) {

            JpaEntityInformation<?, ?> entityInformation = getEntityInformation(information.getDomainType());

            return new NoCriteriaJpaRepository<T, ID>((JpaEntityInformation<T, ?>) entityInformation, entityManager);
        }

        @NonNull
        @Override
        protected Class<?> getRepositoryBaseClass(@NonNull RepositoryMetadata metadata) {
            return NoCriteriaJpaRepository.class;
        }
    }
}
