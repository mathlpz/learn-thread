package com.test.jdk8.bean;

/**
 * @Author: lpz
 * @Date: 2019-04-01 16:42
 */
public class TransactionInfo {

    private int id;
    private int type;
    private int value;

    public TransactionInfo() {
    }

    public TransactionInfo(int type, int value) {
        this.type = type;
        this.value = value;
    }

    public TransactionInfo(int id, int type, int value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
