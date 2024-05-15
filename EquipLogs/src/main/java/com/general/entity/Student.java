package com.general.entity;

public class Student {
    private int id;
    private String name;
    private String surname;
    private String group;
    private String email;
    private String phoneNr;

    public Student(int id, String name, String surname, String group, String email, String phoneNr) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.group = group;
        this.email = email;
        this.phoneNr = phoneNr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }
}
