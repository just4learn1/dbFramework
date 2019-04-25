package com.mzc.db.testEntity;

import com.mzc.db.annotation.SimpleEntity;
import com.mzc.db.annotation.SimpleId;

import java.util.List;
import java.util.Map;

@SimpleEntity
public class Player {
    @SimpleId
    private long id;
    private String username;
    private byte bb1;
    private boolean bb2;
    private int bb3;
    private float bb4;
    private double bb5;
    private int[] aa1;
    private List<Integer> aa2;
    private Map<Integer, String> aa3;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte getBb1() {
        return bb1;
    }

    public void setBb1(byte bb1) {
        this.bb1 = bb1;
    }

    public boolean isBb2() {
        return bb2;
    }

    public void setBb2(boolean bb2) {
        this.bb2 = bb2;
    }

    public int getBb3() {
        return bb3;
    }

    public void setBb3(int bb3) {
        this.bb3 = bb3;
    }

    public float getBb4() {
        return bb4;
    }

    public void setBb4(float bb4) {
        this.bb4 = bb4;
    }

    public double getBb5() {
        return bb5;
    }

    public void setBb5(double bb5) {
        this.bb5 = bb5;
    }

    public int[] getAa1() {
        return aa1;
    }

    public void setAa1(int[] aa1) {
        this.aa1 = aa1;
    }

    public List<Integer> getAa2() {
        return aa2;
    }

    public void setAa2(List<Integer> aa2) {
        this.aa2 = aa2;
    }

    public Map<Integer, String> getAa3() {
        return aa3;
    }

    public void setAa3(Map<Integer, String> aa3) {
        this.aa3 = aa3;
    }
}
