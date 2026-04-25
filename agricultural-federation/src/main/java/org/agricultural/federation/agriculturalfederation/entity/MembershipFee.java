package org.agricultural.federation.agriculturalfederation.entity;

import java.util.Date;

public class MembershipFee {
    private Date eligibleFrom;
    private String frequency;
    private Double amount;
    private String label;
    private String id;
    private Status status;

    public MembershipFee() {
    }

    public MembershipFee(Date eligibleFrom, String frequency, Double amount, String label, String id, Status status) {
        this.eligibleFrom = eligibleFrom;
        this.frequency = frequency;
        this.amount = amount;
        this.label = label;
        this.id = id;
        this.status = status;
    }

    public Date getEligibleFrom() {
        return eligibleFrom;
    }

    public void setEligibleFrom(Date eligibleFrom) {
        this.eligibleFrom = eligibleFrom;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}