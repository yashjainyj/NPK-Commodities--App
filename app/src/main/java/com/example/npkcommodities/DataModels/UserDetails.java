package com.example.npkcommodities.DataModels;

public class UserDetails {
    private String fname;
    private String lname;
    private String phone;
    private String email;
    private String location;
    private String about;
    private String age;
    private String gender;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(String totalMember) {
        this.totalMember = totalMember;
    }

    private String totalMember;
    private String pic_url;
    public UserDetails(String fname, String lname, String phone, String email, String location, String about,String totalMember, String pic_url,String age,String gender) {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;
        this.location = location;
        this.about = about;
        this.totalMember = totalMember;
        this.pic_url = pic_url;
        this.age = age;
        this.gender=gender;
    }
    public UserDetails(String fname, String lname, String phone, String email, String location, String about) {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;
        this.location = location;
        this.about = about;

    }
    public UserDetails() {}

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }
}