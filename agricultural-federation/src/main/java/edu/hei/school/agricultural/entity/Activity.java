package edu.hei.school.agricultural.entity;

import java.time.LocalDate;
import java.util.List;

public class Activity {

    private String id;
    private String collectivityId;
    private String label;
    private ActivityType activityType;
    private List<MemberOccupation> memberOccupationConcerned;
    private MonthlyRecurrenceRule recurrenceRule;
    private LocalDate executiveDate;

    public Activity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollectivityId() {
        return collectivityId;
    }

    public void setCollectivityId(String c) {
        this.collectivityId = c;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType t) {
        this.activityType = t;
    }

    public List<MemberOccupation> getMemberOccupationConcerned() {
        return memberOccupationConcerned;
    }

    public void setMemberOccupationConcerned(List<MemberOccupation> o) {
        this.memberOccupationConcerned = o;
    }

    public MonthlyRecurrenceRule getRecurrenceRule() {
        return recurrenceRule;
    }

    public void setRecurrenceRule(MonthlyRecurrenceRule r) {
        this.recurrenceRule = r;
    }

    public LocalDate getExecutiveDate() {
        return executiveDate;
    }

    public void setExecutiveDate(LocalDate d) {
        this.executiveDate = d;
    }

}
