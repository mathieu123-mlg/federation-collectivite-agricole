package org.agricultural.federation.agriculturalfederation.entity;

public class CreateMemberPayment {
    private Double amount;
    private String membershipFeeIdentifier;
    private String accountCreditedIdentifier;
    private PaymentMode paymentMode;

    public CreateMemberPayment(Double amount, String membershipFeeIdentifier, String accountCreditedIdentifier, PaymentMode paymentMode) {
        this.amount = amount;
        this.membershipFeeIdentifier = membershipFeeIdentifier;
        this.accountCreditedIdentifier = accountCreditedIdentifier;
        this.paymentMode = paymentMode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMembershipFeeIdentifier() {
        return membershipFeeIdentifier;
    }

    public void setMembershipFeeIdentifier(String membershipFeeIdentifier) {
        this.membershipFeeIdentifier = membershipFeeIdentifier;
    }

    public String getAccountCreditedIdentifier() {
        return accountCreditedIdentifier;
    }

    public void setAccountCreditedIdentifier(String accountCreditedIdentifier) {
        this.accountCreditedIdentifier = accountCreditedIdentifier;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }
}
