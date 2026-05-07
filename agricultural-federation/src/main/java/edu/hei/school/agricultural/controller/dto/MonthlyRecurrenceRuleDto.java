package edu.hei.school.agricultural.controller.dto;

public class MonthlyRecurrenceRuleDto {

    private Integer weekOrdinal;
    private String dayOfWeek;

    public MonthlyRecurrenceRuleDto() {
    }

    public Integer getWeekOrdinal() {
        return weekOrdinal;
    }

    public void setWeekOrdinal(Integer w) {
        this.weekOrdinal = w;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String d) {
        this.dayOfWeek = d;
    }
}
