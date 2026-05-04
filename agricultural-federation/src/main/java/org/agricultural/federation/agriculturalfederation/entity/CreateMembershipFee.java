package org.agricultural.federation.agriculturalfederation.entity;

import java.sql.Date;

public class CreateMembershipFee {

    private Date eligibleFrom;
    private String frequency;
    private Double amount;
    private String label;

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

}
