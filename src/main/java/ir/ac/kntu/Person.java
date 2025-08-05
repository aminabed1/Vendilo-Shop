package ir.ac.kntu;

import java.util.*;

public abstract class Person {
    private final static Scanner scan = new Scanner(System.in);
    public static final String RESET = "\u001B[0m";
    public static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";

    private boolean isActive;
    private String name;
    private String surname;
    private String password;
    private Role role;

    public Person(String name, String surname, String password, boolean isActive) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void activePerson() {
        isActive = true;
    }

    public void inactivePerson() {
        isActive = false;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return String.format(
                "\n%s--- User Info ---%s\n" +
                        "%sName        :%s %s\n" +
                        "%sSurname     :%s %s\n" +
                        "%sRole        :%s %s\n" +
                        "%sStatus      :%s %s\n",
                CYAN, RESET,
                YELLOW, RESET, name,
                YELLOW, RESET, surname,
                YELLOW, RESET, role,
                YELLOW, RESET, (isActive ?  GREEN + "Active" : RED +"Inactive") + RESET
        );
    }


}
