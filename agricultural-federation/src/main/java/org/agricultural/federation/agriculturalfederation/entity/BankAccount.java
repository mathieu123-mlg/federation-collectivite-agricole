package org.agricultural.federation.agriculturalfederation.entity;

public class BankAccount implements FinancialAccount {
    private String id;
    private String holderName;
    private Bank bankName;
    private Integer bankCode;
    private Integer bankBranchCode;
    private String bankAccountNumber;
    private Integer bankAccountKey;
    private Double amount;

    public BankAccount() {
    }

    public BankAccount(String id, String holderName, Bank bankName, Integer bankCode, Integer bankBranchCode, String bankAccountNumber, Integer bankAccountKey, Double amount) {
        this.id = id;
        this.holderName = holderName;
        this.bankName = bankName;
        this.bankCode = bankCode;
        this.bankBranchCode = bankBranchCode;
        this.bankAccountNumber = bankAccountNumber;
        this.bankAccountKey = bankAccountKey;
        this.amount = amount;
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

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public Bank getBankName() {
        return bankName;
    }

    public void setBankName(Bank bankName) {
        this.bankName = bankName;
    }

    public Integer getBankCode() {
        return bankCode;
    }

    public void setBankCode(Integer bankCode) {
        this.bankCode = bankCode;
    }

    public Integer getBankBranchCode() {
        return bankBranchCode;
    }

    public void setBankBranchCode(Integer bankBranchCode) {
        this.bankBranchCode = bankBranchCode;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public Integer getBankAccountKey() {
        return bankAccountKey;
    }

    public void setBankAccountKey(Integer bankAccountKey) {
        this.bankAccountKey = bankAccountKey;
    }
}
