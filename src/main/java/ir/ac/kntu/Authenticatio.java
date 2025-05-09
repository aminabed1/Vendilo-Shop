package ir.ac.kntu;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.scene.paint.Color;

public class Authenticatio extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Title
        Label title = new Label("Login");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#39ff14")); // نئونی

        // Username
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(250);

        // Password
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #39ff14; -fx-text-fill: black;");
        loginButton.setMaxWidth(250);

        // Links
        Hyperlink forgotPassword = new Hyperlink("Forgot Password?");
        forgotPassword.setTextFill(Color.web("#00ffff"));

        Hyperlink createAccount = new Hyperlink("Create New Account");
        createAccount.setTextFill(Color.web("#00ffff"));

        // Layout
        VBox layout = new VBox(12, title, usernameField, passwordField, loginButton, forgotPassword, createAccount);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #0f0c29, #302b63, #24243e);");
        layout.setPrefSize(400, 500);

        Scene scene = new Scene(layout);
        primaryStage.setTitle("Neon Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

