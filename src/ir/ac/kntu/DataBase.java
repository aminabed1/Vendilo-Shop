package ir.ac.kntu;

import java.util.*;

public class DataBase {
    private static List<Person> personList = new ArrayList<>();


    public static List<Person> getPersonList() {
        return personList;
    }

    public static void setPersonList(Person person) {
        personList.add(person);
    }


}
