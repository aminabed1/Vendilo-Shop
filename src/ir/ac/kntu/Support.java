package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Support extends Person {

    public Support(String name, String surname, String phoneNumber, String email, String username, String password) {
        super(name, surname, phoneNumber, email, username, password);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != Support.class) {
            return false;
        }

        Support support = (Support) obj;

        return this.getUsername().equals(support.getUsername())
                && this.getPassword().equals(support.getPassword())
                && this.getEmail().equals(support.getEmail())
                && this.getPhoneNumber().equals(support.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getEmail(), getPhoneNumber());
    }
}
