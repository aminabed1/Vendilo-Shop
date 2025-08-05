package ir.ac.kntu;

import java.util.List;

public class SystemMessage {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    public static void printMessage(String message, MessageTitle title) {
        String color = getColor(title);
        System.out.println(color + BOLD + "[" + title + "] " + RESET + color + message + RESET);
    }

    public static void printMessage(List<String> messages, MessageTitle title) {
        String color = getColor(title);
        System.out.println(color + BOLD + "[" + title + "]" + RESET);
        for (String msg : messages) {
            System.out.println(color + "- " + msg + RESET);
        }
    }

    private static String getColor(MessageTitle title) {
        return switch (title) {
            case Error -> RED;
            case Success -> GREEN;
            case Warning -> YELLOW;
            case Info -> BLUE;
            default -> CYAN;
        };
    }
}
