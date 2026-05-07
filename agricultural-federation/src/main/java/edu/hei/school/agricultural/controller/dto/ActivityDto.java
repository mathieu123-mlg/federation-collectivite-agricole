package edu.hei.school.agricultural.controller.dto;

import java.time.LocalDate;

public class ActivityDto {

    private String id;
    private String type;
    private LocalDate activityDate;
    private boolean mandatory;

    public ActivityDto() {
    }

    public ActivityDto(String id, String type, LocalDate activityDate, boolean mandatory) {
        this.id = id;
        this.type = type;
        this.activityDate = activityDate;
        this.mandatory = mandatory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

}
