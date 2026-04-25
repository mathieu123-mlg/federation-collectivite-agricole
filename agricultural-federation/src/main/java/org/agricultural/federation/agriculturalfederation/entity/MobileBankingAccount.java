package org.agricultural.federation.agriculturalfederation.entity;

public class MobileBankingAccount implements FinancialAccount {
    private String id;
    private String holderName;
    private MobileBankingService mobileBankingService;
    private String mobileNumber;
    private Double amount;

    public MobileBankingAccount(String id, String holderName, MobileBankingService mobileBankingService, String mobileNumber, Double amount) {
        this.id = id;
        this.holderName = holderName;
        this.mobileBankingService = mobileBankingService;
        this.mobileNumber = mobileNumber;
        this.amount = amount;
    }

    public MobileBankingAccount() {

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

    public MobileBankingService getMobileBankingService() {
        return mobileBankingService;
    }

    public void setMobileBankingService(MobileBankingService mobileBankingService) {
        this.mobileBankingService = mobileBankingService;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
