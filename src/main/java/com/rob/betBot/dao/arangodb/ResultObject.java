package com.rob.betBot.dao.arangodb;

public class ResultObject<T> {

    private T[] result;
    private boolean hasMore;
    private String id;
    private long count;
    private boolean error;
    private int code;

    public T[] getResult() {
        return result;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public String getId() {
        return id;
    }

    public long getCount() {
        return count;
    }

    public boolean isError() {
        return error;
    }

    public int getCode() {
        return code;
    }
}