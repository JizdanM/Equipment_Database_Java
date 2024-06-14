package com.general.entity;

import java.sql.Date;

public class Logs implements DeletableEntity{
    private int id;
    private Equipment equipment;
    private Student student;
    private Date lendDate;
    private boolean returned;
    private Date returnDate;
    private String note;

    public Logs(int id, Equipment equipment, Student student, Date lendDate, boolean returned, Date returnDate, String note) {
        this.id = id;
        this.equipment = equipment;
        this.student = student;
        this.lendDate = lendDate;
        this.returned = returned;
        this.returnDate = returnDate;
        this.note = note;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Date getLendDate() {
        return lendDate;
    }

    public void setLendDate(Date lendDate) {
        this.lendDate = lendDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    /*
    ** Functions for testing
     */

    @Override
    public String toString() {
        return  "         ID: " + getId() + "\n" +
                "  Equipment: " + getEquipment().getEquipName() + "\n" +
                "  Categorie: " + getEquipment().getCategory() + "\n" +
                "    Student: " + getStudent().getName() + " " + getStudent().getSurname() + "\n" +
                "       Note: " + getNote() + "\n";
    }
}
