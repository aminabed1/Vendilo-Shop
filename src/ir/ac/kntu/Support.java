package ir.ac.kntu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Support extends Person implements Serializable {

    public Support(String name, String surname, String username, String password) {
        //TODO fix here for null phone number and email
        super(name, surname, "11", "11", username, password);
        this.setRole("Support");
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
