package com.sintaks.mushandi.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface BaseService<T> {
    T save(T t,Principal principa);
    T saveNew(T t);
    T update(T t);
    List<T> findAll();
    Optional<T> findById(Long id);
    Boolean deleteById(Long id);
}
