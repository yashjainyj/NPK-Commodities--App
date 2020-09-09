package com.example.npkcommodities.DataModels;

public class MemberModel {
    String name;
    int age;
    String gender;

    public MemberModel() {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public MemberModel(String name, int age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
}
