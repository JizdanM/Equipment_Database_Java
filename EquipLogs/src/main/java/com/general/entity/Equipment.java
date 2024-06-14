package com.general.entity;

public class Equipment implements DeletableEntity {
    private int id;
    private String equipName;
    private String category;
    private int catId;

    public Equipment(int id, String equipName, int catID, String category) {
        this.id = id;
        this.equipName = equipName;
        this.catId = catID;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
