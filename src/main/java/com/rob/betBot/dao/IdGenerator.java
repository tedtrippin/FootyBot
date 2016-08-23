package com.rob.betBot.dao;

public interface IdGenerator {

    public long getNextId(Class<?> clazz);
}
