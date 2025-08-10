package ir.ac.kntu;

import java.util.ArrayList;
import java.util.List;

public class Support extends SpecialUsers {
    private final List<RequestTitle> requestTitles;
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString()).append("\n");
        stringBuilder.append(String.format("%sRequest Titles (%d):%s\n", YELLOW, requestTitles.size(), RESET));
        for (RequestTitle rt : requestTitles) {
            stringBuilder.append("  - ").append(rt.toString().replace("_", " ")).append("\n");
        }
        return stringBuilder.toString();
    }
}
