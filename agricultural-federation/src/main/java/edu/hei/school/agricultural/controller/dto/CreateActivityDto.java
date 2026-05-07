package edu.hei.school.agricultural.controller.dto;

import java.time.LocalDate;

public class CreateActivityDto {

    private String type;
    private LocalDate activityDate;
    private boolean mandatory;

    public CreateActivityDto() {
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
