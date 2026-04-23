package com.progresionnacion.model;

import java.time.LocalDate;

public class TimeRecord {

    private int id;
    private int swimmerId;
    private String swimmerName;
    private String swimmerSex;
    private String swimmerCategory;
    private LocalDate recordDate;
    private String stroke;
    private String sessionType;
    private double timeSeconds;
    private String location;
    private String coachComment;
    private String coachName;

    public TimeRecord() {
    }

    public TimeRecord(int id, int swimmerId, String swimmerName, String swimmerSex, String swimmerCategory,
                      LocalDate recordDate, String stroke, String sessionType, double timeSeconds,
                      String location, String coachComment, String coachName) {
        this.id = id;
        this.swimmerId = swimmerId;
        this.swimmerName = swimmerName;
        this.swimmerSex = swimmerSex;
        this.swimmerCategory = swimmerCategory;
        this.recordDate = recordDate;
        this.stroke = stroke;
        this.sessionType = sessionType;
        this.timeSeconds = timeSeconds;
        this.location = location;
        this.coachComment = coachComment;
        this.coachName = coachName;
    }

    public int getId() {
        return id;
    }

    public int getSwimmerId() {
        return swimmerId;
    }

    public String getSwimmerName() {
        return swimmerName;
    }

    public String getSwimmerSex() {
        return swimmerSex;
    }

    public String getSwimmerCategory() {
        return swimmerCategory;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public String getStroke() {
        return stroke;
    }

    public String getSessionType() {
        return sessionType;
    }

    public double getTimeSeconds() {
        return timeSeconds;
    }

    public String getLocation() {
        return location;
    }

    public String getCoachComment() {
        return coachComment;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSwimmerId(int swimmerId) {
        this.swimmerId = swimmerId;
    }

    public void setSwimmerName(String swimmerName) {
        this.swimmerName = swimmerName;
    }

    public void setSwimmerSex(String swimmerSex) {
        this.swimmerSex = swimmerSex;
    }

    public void setSwimmerCategory(String swimmerCategory) {
        this.swimmerCategory = swimmerCategory;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public void setTimeSeconds(double timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCoachComment(String coachComment) {
        this.coachComment = coachComment;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }
}
