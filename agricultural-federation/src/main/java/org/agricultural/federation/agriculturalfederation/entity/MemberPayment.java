package org.agricultural.federation.agriculturalfederation.entity;

import java.time.Instant;

public class MemberPayment {
    private String id;
    private Double amount;
    private PaymentMode paymentMode;
    private FinancialAccount accountCredited;
    private Instant creationDate;

    public MemberPayment(String id, Double amount, PaymentMode paymentMode, FinancialAccount accountCredited, Instant creationDate) {
        this.id = id;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.accountCredited = accountCredited;
        this.creationDate = creationDate;
    }

    public MemberPayment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public FinancialAccount getAccountCredited() {
        return accountCredited;
    }

    public void setAccountCredited(FinancialAccount accountCredited) {
        this.accountCredited = accountCredited;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }
}
