package com.progresionnacion.model;

import java.time.LocalDate;

public class RecordFilter {

    private Integer swimmerId;
    private String sex;
    private String category;
    private String stroke;
    private String sessionType;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String searchText;

    public Integer getSwimmerId() {
        return swimmerId;
    }

    public void setSwimmerId(Integer swimmerId) {
        this.swimmerId = swimmerId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStroke() {
        return stroke;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
