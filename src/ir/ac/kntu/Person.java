package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;

public abstract class Person {
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    private int age;
    private String gender;
    private List<String> errorList;


    public Person(String name, String surname, String phoneNumber, String email, String username, String password) {

        errorList = new ArrayList<>();
        InfoValidator cv = new InfoValidator();
        if (cv.isPersonInfoValid(name, surname, phoneNumber, email, username, password, errorList)) {
            this.name = name;
            this.surname = surname;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.username = username;
            this.password = password;
            DataBase.setPersonList(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public abstract boolean equals(Object object);

    public abstract int hashCode();
}
