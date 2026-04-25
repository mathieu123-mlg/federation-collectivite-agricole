package org.agricultural.federation.agriculturalfederation.entity;

import java.time.Instant;

public class CollectivityTransaction {
    private Integer id;
    private Instant creationDate;
    private Double amount;
    private PaymentMode paymentMode;
    private FinancialAccount accountCredited;
    private Member memberDebited;

    public CollectivityTransaction(Integer id, Instant creationDate, Double amount, PaymentMode paymentMode, FinancialAccount accountCredited, Member memberDebited) {
        this.id = id;
        this.creationDate = creationDate;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.accountCredited = accountCredited;
        this.memberDebited = memberDebited;
    }

    public CollectivityTransaction() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
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

    public Member getMemberDebited() {
        return memberDebited;
    }

    public void setMemberDebited(Member memberDebited) {
        this.memberDebited = memberDebited;
    }
}
