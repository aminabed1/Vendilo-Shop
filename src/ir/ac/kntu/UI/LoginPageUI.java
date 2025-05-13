package ir.ac.kntu.UI;

import ir.ac.kntu.*;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class LoginPageUI extends Application {
    private String selectedRole = "Customer";

    @Override
    public void start(Stage primaryStage) {
        HBox root = new HBox();
        root.setStyle("-fx-background-color: linear-gradient(to right, #f8f9fa, #e9ecef);");

        VBox formContainer = new VBox();
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(40));
        formContainer.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Label title = new Label("VENDILO");
        title.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_BOLD, 36));
        title.setTextFill(Color.web("#2c3e50"));
        title.setPadding(new Insets(0, 0, 30, 0));

        ToggleGroup roleGroup = new ToggleGroup();
        ToggleButton customerTab = createRoleTab("Customer", roleGroup);
        ToggleButton sellerTab = createRoleTab("Seller", roleGroup);
        ToggleButton supportTab = createRoleTab("Support", roleGroup);
        customerTab.setSelected(true);

        HBox tabs = new HBox(10, sellerTab, customerTab, supportTab);
        tabs.setAlignment(Pos.CENTER);
        tabs.setPadding(new Insets(0, 0, 20, 0));

        TextField authentication = new TextField();
        authentication.setPromptText("Username, email or phone");
        authentication.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-padding: 12; -fx-font-size: 14px;");
        authentication.setMaxWidth(350);

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8; -fx-padding: 12; -fx-font-size: 14px;");
        password.setMaxWidth(350);

        Button loginBtn = new Button("LOGIN");
        loginBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-padding: 12 30; -fx-font-size: 16px;");
        loginBtn.setMaxWidth(350);
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 30; -fx-font-size: 16px;"));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12 30; -fx-font-size: 16px;"));

        HBox linksContainer = new HBox(15);
        linksContainer.setAlignment(Pos.CENTER);
        Hyperlink createAcc = new Hyperlink("Create account");
        createAcc.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 13px;");
        Hyperlink forgotPass = new Hyperlink("Forgot password?");
        forgotPass.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 13px;");
        linksContainer.getChildren().addAll(createAcc, forgotPass);

        Label errorMessage = new Label();
        errorMessage.setTextFill(Color.web("#e74c3c"));
        errorMessage.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        errorMessage.setPadding(new Insets(10, 0, 0, 0));

        VBox form = new VBox(15, title, tabs, authentication, password, loginBtn, linksContainer, errorMessage);
        form.setAlignment(Pos.CENTER);
        form.setMaxWidth(400);

        formContainer.getChildren().add(form);

        StackPane imagePane = new StackPane();
        imagePane.setAlignment(Pos.CENTER);
        imagePane.setStyle("-fx-background-color: #ecf0f1;");

        ImageView logoView = new ImageView(new Image("file:F:/advancedProgramming/untitled/iamges/loginpage.jpg"));
        logoView.setFitWidth(500);
        logoView.setPreserveRatio(true);
        logoView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        imagePane.getChildren().add(logoView);

        root.getChildren().addAll(formContainer, imagePane);
        HBox.setHgrow(formContainer, Priority.ALWAYS);
        HBox.setHgrow(imagePane, Priority.ALWAYS);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Vendilo - Login");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();


        createAcc.setOnAction(event -> {
            CreateAccountUI createAccountUI = new CreateAccountUI();
            Stage stage = new Stage();
            createAccountUI.start(stage);
            ((Stage) ((Hyperlink) event.getSource()).getScene().getWindow()).close();
        });

        loginBtn.setOnAction(e -> {
            String authenticationText = authentication.getText();
            String passwordText = password.getText();

            if (authenticationText.isEmpty() || passwordText.isEmpty()) {
                showFloatingMessage("Please fill in all fields.", true, primaryStage, null);
                return;
            }

            if (LoginPage.receiveLoginInfo(authenticationText, passwordText, selectedRole)) {
                Person person = findPerson(authenticationText, passwordText, selectedRole);
                handleLoginForRole(person, selectedRole, primaryStage, e);
            } else {
                showFloatingMessage("Invalid credentials. Please try again.", true, primaryStage, null);
            }
        });
    }

    private ToggleButton createRoleTab(String role, ToggleGroup group) {
        ToggleButton tab = new ToggleButton(role);
        tab.setToggleGroup(group);
        tab.setPrefWidth(120);
        tab.setStyle("-fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 14px; " +
                "-fx-padding: 8 15; -fx-background-color: #ecf0f1; -fx-text-fill: #7f8c8d;");

        tab.setOnAction(e -> {
            selectedRole = role;
            updateTabStyle(group);
        });

        tab.setOnMouseEntered(e -> {
            if (!tab.isSelected()) {
                tab.setStyle("-fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 14px; " +
                        "-fx-padding: 8 15; -fx-background-color: #dfe6e9; -fx-text-fill: #7f8c8d;");
            }
        });

        tab.setOnMouseExited(e -> {
            if (!tab.isSelected()) {
                tab.setStyle("-fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 14px; " +
                        "-fx-padding: 8 15; -fx-background-color: #ecf0f1; -fx-text-fill: #7f8c8d;");
            }
        });

        return tab;
    }

    private void updateTabStyle(ToggleGroup group) {
        for (Toggle toggle : group.getToggles()) {
            ToggleButton btn = (ToggleButton) toggle;
            if (btn.isSelected()) {
                btn.setStyle("-fx-background-color: #3498db; -fx-background-radius: 8; " +
                        "-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 8 15; " +
                        "-fx-text-fill: white;");
            } else {
                btn.setStyle("-fx-background-radius: 8; -fx-font-weight: bold; -fx-font-size: 14px; " +
                        "-fx-padding: 8 15; -fx-background-color: #ecf0f1; -fx-text-fill: #7f8c8d;");
            }
        }
    }

    private void showFloatingMessage(String message, boolean isError, Stage ownerStage, Runnable afterMessage) {
        Popup popup = new Popup();

        Label messageLabel = new Label(message);
        messageLabel.setStyle(
                "-fx-background-color: " + (isError ? "rgba(231,76,60,0.9);" : "rgba(46,204,113,0.9);") +
                        " -fx-text-fill: white; -fx-padding: 12 20; -fx-font-weight: bold; " +
                        "-fx-background-radius: 8; -fx-font-size: 14px;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(300);

        StackPane content = new StackPane(messageLabel);
        content.setStyle("-fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 0);");
        popup.getContent().add(content);

        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.show(ownerStage,
                ownerStage.getX() + ownerStage.getWidth()/2 - 150,
                ownerStage.getY() + 50);

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            popup.hide();
            if (afterMessage != null) {
                afterMessage.run();
            }
        });
        delay.play();
    }

    private Person findPerson(String authenticationText, String password, String selectedRole) {
        List<Person> personList = DataBase.getPersonList();

        for (Person person : personList) {
            if (authenticate(person, authenticationText, password)) {
                return person;
            }
        }
        return null;
    }

    private boolean authenticate(Person person, String authenticationText, String password) {
        return (person.getUsername().equals(authenticationText)
                || person.getPhoneNumber().equals(authenticationText)
                || person.getEmail().equals(authenticationText))
                && person.getPassword().equals(password);
    }

    private void handleLoginForRole(Person person, String role, Stage primaryStage, javafx.event.ActionEvent e) {
        boolean success = switch (role) {
            case "Customer" -> person instanceof Customer;
            case "Seller" -> person instanceof Seller;
            default -> false;
        };

        if (!success) {
            showFloatingMessage("Entered data is not for a " + role, true, primaryStage, null);
        } else {
            showFloatingMessage("Welcome, " + person.getName(), false, primaryStage, () -> {
                new MainPageUI(person).start(new Stage());
                ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
            });
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}