package com.phildev.pmb.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Pattern(regexp ="^[A-Z][a-z]+$", message="Firstname should start with capital letter followed by lowercase letter(s)")
    @Column(name = "firstname", nullable = false)
    private String firstName;


    @Pattern(regexp ="^[A-Z][a-z]+$", message="Lastname should start with capital letter followed by lowercase letter(s)")
    @Column(name = "lastname", nullable = false)
    private String lastName;


    @Column(name = "role", nullable = false)
    private String role;

    @Pattern(regexp="^[\\w.-]+@[a-zA-Z\\d.-]+.[a-zA-Z]{2,6}$", message="Invalid email format")
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    @JoinColumn(name = "sender_email")
    private List<Connection> connections;

    public User(){
        this.role = "USER";
        this.connections = new ArrayList<>();
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = "USER";
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }


}