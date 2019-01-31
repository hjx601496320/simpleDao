package com.hebaibai.jdbcplus.entity;

public class Foo {


    String ID;
    String REQUEST_TYPE;


    public String getREQUEST_TYPE() {
        return REQUEST_TYPE;
    }

    public void setREQUEST_TYPE(String REQUEST_TYPE) {
        this.REQUEST_TYPE = REQUEST_TYPE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "Foo{" +
                "ID='" + ID + '\'' +
                ", REQUEST_TYPE='" + REQUEST_TYPE + '\'' +
                '}';
    }
}
