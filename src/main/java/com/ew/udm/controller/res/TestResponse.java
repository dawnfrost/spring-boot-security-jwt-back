package com.ew.udm.controller.res;

public class TestResponse {
    private int intField;
    private long longField;
    private String strField;
    private double doubleField;

    public TestResponse() {
    }

    public TestResponse(int intField, long longField, String strField, double doubleField) {
        this.intField = intField;
        this.longField = longField;
        this.strField = strField;
        this.doubleField = doubleField;
    }

    public int getIntField() {
        return intField;
    }

    public void setIntField(int intField) {
        this.intField = intField;
    }

    public long getLongField() {
        return longField;
    }

    public void setLongField(long longField) {
        this.longField = longField;
    }

    public String getStrField() {
        return strField;
    }

    public void setStrField(String strField) {
        this.strField = strField;
    }

    public double getDoubleField() {
        return doubleField;
    }

    public void setDoubleField(double doubleField) {
        this.doubleField = doubleField;
    }
}
