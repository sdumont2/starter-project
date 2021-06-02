package com.dumontsean.data.support.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.ogm.exception.NotSupportedException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.Collections;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

public class NoCriteriaJpaRepository<T, I extends Serializable> extends SimpleJpaRepository<T, I> {

    private static final Logger log = LogManager.getLogger(NoCriteriaJpaRepository.class);

    private final EntityManager em;

    /**
     * Creates a {@link TypedQuery} for the given {@link Specification} and {@link Sort}.
     *
     * @param spec        can be {@literal null}.
     * @param domainClass must not be {@literal null}.
     * @param sort        must not be {@literal null}.
     * @return
     */
    protected <S extends T> TypedQuery<S> getQuery(@Nullable Specification<S> spec, Class<S> domainClass, Sort sort) {
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<S> query = builder.createQuery(domainClass);

            Root<S> root = applySpecificationToCriteria(spec, domainClass, query);
            query.select(root);

            if (sort.isSorted()) {
                query.orderBy(toOrders(sort, root, builder));
            }

            return applyRepositoryMethodMetadata(em.createQuery(query));
        } catch (NotSupportedException nse) {
            //If we catch this then something went wrong. Try to use a basic JPQL Query
            //TODO Better criteria to JPQL
            return em.createQuery("SELECT a FROM " + domainClass.getSimpleName() + " a", domainClass);
        }
    }

    /**
     * Creates a new count query for the given {@link Specification}.
     *
     * @param spec        can be {@literal null}.
     * @param domainClass must not be {@literal null}.
     * @return
     */
    protected <S extends T> TypedQuery<Long> getCountQuery(@Nullable Specification<S> spec, Class<S> domainClass) {
        try {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);

            Root<S> root = applySpecificationToCriteria(spec, domainClass, query);

            if (query.isDistinct()) {
                query.select(builder.countDistinct(root));
            } else {
                query.select(builder.count(root));
            }

            // Remove all Orders the Specifications might have applied
            query.orderBy(Collections.<Order>emptyList());

            return em.createQuery(query);
        } catch (NotSupportedException nse) {
            //If we catch this then something went wrong. Try to use a basic JPQL Query
            //TODO Better criteria to JPQL
            return em.createQuery("SELECT COUNT(a) FROM " + domainClass.getSimpleName() + " a", Long.class);
        }
    }

    // Copied Private methods

    /**
     * Applies the given {@link Specification} to the given {@link CriteriaQuery}.
     *
     * @param spec        can be {@literal null}.
     * @param domainClass must not be {@literal null}.
     * @param query       must not be {@literal null}.
     * @return
     */
    private <S, U extends T> Root<U> applySpecificationToCriteria(@Nullable Specification<U> spec, Class<U> domainClass,
                                                                  CriteriaQuery<S> query) {

        Assert.notNull(domainClass, "Domain class must not be null!");
        Assert.notNull(query, "CriteriaQuery must not be null!");

        Root<U> root = query.from(domainClass);

        if (spec == null) {
            return root;
        }

        CriteriaBuilder builder = em.getCriteriaBuilder();
        Predicate predicate = spec.toPredicate(root, query, builder);

        if (predicate != null) {
            query.where(predicate);
        }

        return root;
    }

    private <S> TypedQuery<S> applyRepositoryMethodMetadata(TypedQuery<S> query) {

        if (getRepositoryMethodMetadata() == null) {
            return query;
        }

        LockModeType type = getRepositoryMethodMetadata().getLockModeType();
        TypedQuery<S> toReturn = type == null ? query : query.setLockMode(type);

        applyQueryHints(toReturn);

        return toReturn;
    }

    private void applyQueryHints(Query query) {
        getQueryHints().withFetchGraphs(em).forEach(query::setHint);
    }

    // End Copied Private Methods

    /**
     * Creates a new {@link SimpleJpaRepository} to manage objects of the given {@link JpaEntityInformation}.
     *
     * @param entityInformation must not be {@literal null}.
     * @param entityManager     must not be {@literal null}.
     */
    public NoCriteriaJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);

        this.em = entityManager;
    }
}
