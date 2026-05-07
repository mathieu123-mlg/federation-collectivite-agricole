package edu.hei.school.agricultural.entity;

public class MonthlyRecurrenceRule {

    private Integer weekOrdinal;
    private String dayOfWeek;

    public MonthlyRecurrenceRule() {
    }

    public Integer getWeekOrdinal() {
        return weekOrdinal;
    }

    public void setWeekOrdinal(Integer weekOrdinal) {
        this.weekOrdinal = weekOrdinal;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
