package com.rob.betBot.dao.arangodb;

import com.rob.betBot.conf.Property;
import com.rob.betBot.conf.PropertyKey;
import com.rob.betBot.exception.CommunicatorException;
import com.rob.betBot.http.JsonCommunicator;

public class ArangoDbCommunicator extends JsonCommunicator {

    private final String METHOD_DOCUMENT = "_api/document/%s";
    private final String METHOD_DOCUMENT_WITH_COLLECTION = "_api/document/%s?collection=%s";
    private final String METHOD_COLLECTION = "_api/simple/all?collection=%s";

    ArangoDbCommunicator () {
        super(getUrl());
    }

    public String getValue(long id)
        throws ArangoDbException {

        return doGetValue(String.format(METHOD_DOCUMENT, id));
    }

    public String getValue(long id, String collection)
        throws ArangoDbException {

        return doGetValue(String.format(METHOD_DOCUMENT_WITH_COLLECTION, id, collection));
    }

    public String getCollection(String collection)
        throws ArangoDbException {

        try {
            String response = get(String.format(METHOD_COLLECTION, collection), null);
            return response;
        } catch (CommunicatorException ex) {
            throw new ArangoDbException("HTTP Error communicating with ArangoDB", ex);
        }

    }

    public String setValue(long id, String json)
        throws ArangoDbException {

        return doSetValue(String.format(METHOD_DOCUMENT, id), json);
    }

    public String setValue(long id, String json, String collection)
        throws ArangoDbException {

        return doSetValue(String.format(METHOD_DOCUMENT_WITH_COLLECTION, id, collection), json);
    }

    public void delete(long id, String collection)
        throws ArangoDbException {

        doDelete(String.format(METHOD_DOCUMENT, id, collection));
    }

    protected static String getUrl() {
        return Property.getProperty(PropertyKey.ARANGODB_URL);
    }

    private String doGetValue(String method)
        throws ArangoDbException {

        try {
            return get(method, null);
        } catch (CommunicatorException ex) {
            throw new ArangoDbException("HTTP Error communicating with ArangoDB", ex);
        }
    }

    private String doSetValue(String method, String json)
        throws ArangoDbException {

        try {
            return post(json, method, null);
        } catch (CommunicatorException ex) {
            throw new ArangoDbException("HTTP Error communicating with ArangoDB", ex);
        }
    }

    private void doDelete(String method)
        throws ArangoDbException {

        try {
            delete(method, null);
        } catch (CommunicatorException ex) {
            throw new ArangoDbException("HTTP Error communicating with ArangoDB", ex);
        }
    }
}
