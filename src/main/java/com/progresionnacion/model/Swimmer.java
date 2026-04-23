package com.progresionnacion.model;

import java.time.LocalDate;

public class Swimmer {

    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String sex;
    private String category;
    private String club;
    private String notes;

    public Swimmer() {
    }

    public Swimmer(int id, String firstName, String lastName, LocalDate birthDate, String sex, String category, String club, String notes) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.sex = sex;
        this.category = category;
        this.club = club;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getSex() {
        return sex;
    }

    public String getCategory() {
        return category;
    }

    public String getClub() {
        return club;
    }

    public String getNotes() {
        return notes;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return getFullName();
    }
}
