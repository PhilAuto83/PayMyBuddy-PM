package com.phildev.pmb.model;


import jakarta.persistence.*;

@Entity
@Table(name = "account")
public class Account {

    @Id
    private int id;

    private double balance;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
