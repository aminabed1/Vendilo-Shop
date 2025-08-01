package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;

public class Support extends SpecialUsers {
    List<RequestTitle> requestTitles;
    public Support(String name, String surname, String username, String password) {
        super(name, surname, username, password);
        requestTitles = new ArrayList<RequestTitle>();
        this.setRole(Role.Support);
    }

    public void addRequestTitle(RequestTitle requestTitle) {
        requestTitles.add(requestTitle);
    }

    public void removeRequestTitle(RequestTitle requestTitle) {
        requestTitles.remove(requestTitle);
    }

    public List<RequestTitle> getRequestTitles() {
        return requestTitles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("\n");
        sb.append(String.format("%sRequest Titles (%d):%s\n", YELLOW, requestTitles.size(), RESET));
        for (RequestTitle rt : requestTitles) {
            sb.append("  - ").append(rt.toString()).append("\n");
        }
        return sb.toString();
    }

}
