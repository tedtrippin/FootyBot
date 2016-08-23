package com.rob.betBot.dao.arangodb;

import java.util.Set;

import javax.persistence.PersistenceException;

import com.rob.betBot.dao.GenericDao;
import com.rob.betBot.model.DataObject;
import com.rob.betBot.util.JsonUtils;

public abstract class AbstractArangoDao<T extends DataObject> implements GenericDao<T> {

    private final ArangoDbCommunicator arangoCommunicator = new ArangoDbCommunicator();

    @Override
    public T getById(long id)
        throws PersistenceException {

        try {
            String json = arangoCommunicator.getValue(id, getCollection());
            return JsonUtils.readValue(json, getDataClass());
        } catch (ArangoDbException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void saveOrUpdate(T t)
        throws PersistenceException {

        try {
            String json = JsonUtils.toJsonString(t);
            arangoCommunicator.setValue(t.getId(), json, getCollection());
        } catch (ArangoDbException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void saveAll(Set<T> ts)
        throws PersistenceException {

        try {
            for (T t : ts) {
                String json = JsonUtils.toJsonString(t);
                arangoCommunicator.setValue(t.getId(), json, getCollection());
            }
        } catch (ArangoDbException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void delete(T t)
        throws PersistenceException {

        try {
            arangoCommunicator.delete(t.getId(), getCollection());
        } catch (ArangoDbException ex) {
            throw new PersistenceException(ex);
        }
    }

    abstract protected Class<T> getDataClass();

    abstract protected String getCollection();
}
