package com.rob.betBot.dao;

import java.util.Collection;
import java.util.Set;

import com.rob.betBot.model.DataObject;

public interface GenericDao<T extends DataObject> {

    public T getById(long id);

    public Collection<T> getAll();

    public void saveOrUpdate(T t);

    public void saveAll(Set<T> ts);

    public void delete(T t);
}
