package com.phildev.pmb.dto;


public class TransactionDTO {

    private String connectionName;
    private String Description;
    private double amount;

    public TransactionDTO(String connectionName, String description, double amount) {
        this.connectionName = connectionName;
        Description = description;
        this.amount = amount;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "connectionName='" + connectionName + '\'' +
                ", Description='" + Description + '\'' +
                ", amount=" + amount +
                '}';
    }
}
