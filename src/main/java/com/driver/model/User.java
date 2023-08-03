package com.driver.model;

import java.util.UUID;

public class User {

    private UUID aadharCardNo; //This is the unique key that determines a unique user
    private String name;
    private int age;

    public User(UUID aadharCardNo, String name, int age) {
        this.aadharCardNo = aadharCardNo;
        this.name = name;
        this.age = age;
    }

    public UUID getaadharCardNo() {
        return aadharCardNo;
    }

    public void setaadharCardNo(int aadharCardNo) {
        this.aadharCardNo = aadharCardNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
