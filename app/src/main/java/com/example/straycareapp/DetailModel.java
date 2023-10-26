/*
 Created by Intellij IDEA
 Author Name: KULDEEP SINGH (kuldeep506)
 Date: 11-05-2022
*/

package com.example.straycareapp;

public class DetailModel {
    private String animalType;
    private String gender;
    private String description;
    private String condition;
    private String address;
    private String city;
    private String senderName;
    private String phoneNumber;
    private String imageUri;

    public DetailModel() {
    }

    public DetailModel(String animalType, String gender, String description,String condition,
                       String address, String city, String senderName, String phoneNumber,String imageUri) {
        this.animalType = animalType;
        this.gender = gender;
        this.description = description;
        this.condition = condition;
        this.address = address;
        this.city = city;
        this.senderName = senderName;
        this.phoneNumber = phoneNumber;
        this.imageUri = imageUri;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

}
