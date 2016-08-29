package com.hugopicado.stocks.repository;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * Generic repository interface to abstract the common operations to the
 * underlying datastore.
 *
 * @param <T> Entity type.
 */
public interface GenericRepository<T> {

    /**
     * Retrieves all entities.
     *
     * @return collection of entities.
     */
    Collection<T> findAll();

    /**
     * Retrieves entities that respect a specification criteria.
     *
     * @param spec specification
     * @return collection of entities
     */
    Collection<T> findAll(Predicate<T> spec);

    /**
     * Retrieves the first occurrence respecting a specification criteria.
     *
     * @param spec specification
     * @return entity
     */
    T findOne(Predicate<T> spec);

    /**
     * Saves an entity into the repository.
     *
     * @param entity entity
     */
    void save(T entity);

    /**
     * Deletes an entity from the repository.
     *
     * @param entity entity
     */
    void delete(T entity);

}
