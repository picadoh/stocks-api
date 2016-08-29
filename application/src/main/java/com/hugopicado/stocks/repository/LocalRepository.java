package com.hugopicado.stocks.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Local implementation of a repository that stores entities in
 * a hash set.
 *
 * @param <T> Entity type.
 */
abstract class LocalRepository<T> implements GenericRepository<T> {
    private final Set<T> entities;

    LocalRepository() {
        this.entities = newHashSet();
    }

    @Override
    public Collection<T> findAll() {
        return entities;
    }

    @Override
    public List<T> findAll(Predicate<T> spec) {
        return entities.stream().filter(spec).collect(Collectors.toList());
    }

    @Override
    public T findOne(Predicate<T> spec) {
        return entities.stream().filter(spec).findFirst().orElse(null);
    }

    @Override
    public void save(T entity) {
        this.entities.add(entity);
    }

    @Override
    public void delete(T entity) {
        this.entities.remove(entity);
    }

    public void clear() {
        this.entities.clear();
    }
}
