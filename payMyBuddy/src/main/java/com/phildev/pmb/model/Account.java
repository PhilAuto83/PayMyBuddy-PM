package com.phildev.pmb.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @NotNull
    private int id;

    @NotNull
    private double balance;

    public Account(){

    }

    public Account(int id){
        this.id=id;
        this.balance =0.0;
    }


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

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}

