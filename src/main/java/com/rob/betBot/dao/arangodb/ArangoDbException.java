package com.rob.betBot.dao.arangodb;

public class ArangoDbException extends Exception {

    private static final long serialVersionUID = 1L;

    public ArangoDbException (String message) {
        super(message);
    }

    public ArangoDbException (String message, Exception innerException) {
        super(message, innerException);
    }
}
