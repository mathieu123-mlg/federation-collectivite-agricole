package org.agricultural.federation.agriculturalfederation.entity;

public class CashAccount implements FinancialAccount {
    private Integer id;
    private Double Amount;

    public CashAccount(Integer id, Double amount) {
        this.id = id;
        Amount = amount;
    }

    public CashAccount() {

    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Double getAmount() {
        return Amount;
    }

    @Override
    public void setAmount(Double amount) {
        Amount = amount;
    }
}
