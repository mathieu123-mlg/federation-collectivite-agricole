package org.agricultural.federation.agriculturalfederation.entity;

public class CashAccount implements FinancialAccount {
    private String id;
    private Double amount;

    public CashAccount(String id, Double amount) {
        this.id = id;
        this.amount = amount;
    }

    public CashAccount() {

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Double getAmount() {
        return amount;
    }

    @Override
    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
