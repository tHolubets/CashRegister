package com.studing.cashRegister.model;

import java.io.Serializable;

/**
 * Model class of user.
 * @author tHolubets
 */
public class User implements Serializable {
    private long id;
    private String login;
    private String password;
    private UserRole role;
    private String firstName;
    private String surname;
    private String fatherName;

    /**
     * Constructor of user class with all parameters
     * @param id user id
     * @param login user login
     * @param password user password
     * @param role user role
     * @param firstName user first name
     * @param surname user surname
     * @param fatherName user father name
     */
    public User(long id, String login, String password, UserRole role, String firstName, String surname, String fatherName) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.surname = surname;
        this.fatherName = fatherName;
    }

    /**
     * Constructor of user class with shortened list of parameters, should be used for login check
     * @param login user login
     * @param password user password
     */
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    /**
     * Method to get full name of user
     * @return full name of user in format [surname first_name father_name]
     */
    public String getFullName(){
        return surname + " " + firstName + " " + fatherName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", fatherName='" + fatherName + '\'' +
                '}';
    }
}
