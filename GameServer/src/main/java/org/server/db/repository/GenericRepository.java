package org.server.db.repository;

import java.util.List;

public interface GenericRepository<E,K> {

    E create(E e);
    void delete(K k);
    E update(E e);
    E get(K k);
    List<E> getAll();

}
