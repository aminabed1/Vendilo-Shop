package ir.ac.kntu.UI;

import ir.ac.kntu.Customer;
import ir.ac.kntu.DataBase;
import ir.ac.kntu.LoginPage;
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
        // Title
        Label title = new Label("VENDILO");
        title.setFont(Font.font("Cinzel", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#b63227"));


        ToggleGroup roleGroup = new ToggleGroup();

        ToggleButton customerTab = createRoleTab("Customer", roleGroup);
        ToggleButton sellerTab = createRoleTab("Seller", roleGroup);
        ToggleButton supportTab = createRoleTab("Support", roleGroup);

        customerTab.setSelected(true);

        HBox tabs = new HBox(sellerTab, customerTab, supportTab);
        tabs.setAlignment(Pos.CENTER);
        tabs.setSpacing(10);
        tabs.setPadding(new Insets(10));

        TextField username = new TextField();
        username.setPromptText("username");

        PasswordField password = new PasswordField();
        password.setPromptText("password");

        username.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #cd0909;");
        password.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #cd0909;");

        Button loginBtn = new Button("login");
        loginBtn.setStyle("-fx-background-color: #d70041; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");

        Hyperlink createAcc = new Hyperlink("create new account");
        Hyperlink forgotPass = new Hyperlink("forgot password");


        createAcc.setOnAction(event -> {
            CreateAccountUI createAccountUI = new CreateAccountUI();
            Stage stage = new Stage();
            createAccountUI.start(stage);
            ((Stage) ((Hyperlink) event.getSource()).getScene().getWindow()).close();
        });

        Label errorMessage = new Label();
        errorMessage.setTextFill(Color.RED);
        errorMessage.setFont(Font.font("Arial", 12));

        VBox form = new VBox(15, title, tabs, username, password, loginBtn, createAcc, forgotPass, errorMessage);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setPadding(new Insets(60, 40, 60, 60));
        form.setMaxWidth(400);

        StackPane leftPane = new StackPane(form);
        leftPane.setStyle("-fx-background-color: #f5e6cc;");

        ImageView logoView = new ImageView(new Image("file:F:/advancedProgramming/untitled/iamges/loginpage.jpg"));
        logoView.setFitWidth(400);
        logoView.setPreserveRatio(true);
        StackPane rightPane = new StackPane(logoView);
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setStyle("-fx-background-color: #f8ebdc;");
        rightPane.setPadding(new Insets(20));

        HBox root = new HBox(leftPane, rightPane);
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.ALWAYS);

        Scene scene = new Scene(root, 960, 600);
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();

        loginBtn.setOnAction(e -> {
            String usernameText = username.getText();
            String passwordText = password.getText();

            /////TODO complete here ;
            if (LoginPage.receiveLoginInfo(usernameText, passwordText, selectedRole)) {
                showFloatingMessage("Logged in successfully,\nWelcome dear " + selectedRole, false, primaryStage, () -> {
                    Customer customer = findCustomer(usernameText, passwordText);
                    new MainPageUI(customer).start(new Stage());
                    ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
                });

            } else {
                showFloatingMessage("Invalid username or password\ndon't have account? create one", true, primaryStage, null);
            }
        });
    }

    private ToggleButton createRoleTab(String role, ToggleGroup group) {
        ToggleButton tab = new ToggleButton(role);
        tab.setToggleGroup(group);
        tab.setPrefWidth(120);
        tab.setStyle("-fx-background-radius: 10; -fx-font-weight: bold;");

        tab.setOnAction(e -> {
            selectedRole = role;
            updateTabStyle(group);
        });

        return tab;
    }

    private void updateTabStyle(ToggleGroup group) {
        for (Toggle toggle : group.getToggles()) {
            ToggleButton btn = (ToggleButton) toggle;
            if (btn.isSelected()) {
                btn.setStyle("-fx-background-color: #f1d9b5; -fx-background-radius: 10; -fx-font-weight: bold;");
            } else {
                btn.setStyle("-fx-background-color: #f5e6cc; -fx-background-radius: 10; -fx-font-weight: normal;");
            }
        }
    }

    private void showFloatingMessage(String message, boolean isError, Stage ownerStage, Runnable afterMessage) {
        Popup popup = new Popup();

        Label messageLabel = new Label(message);
        messageLabel.setStyle(
                "-fx-background-color: " + (isError ? "rgba(255,0,0,0.85);" : "rgba(0,128,0,0.85);") +
                        " -fx-text-fill: white; -fx-padding: 10; -fx-font-weight: bold; -fx-background-radius: 5;"
        );
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(300);

        StackPane content = new StackPane(messageLabel);
        content.setStyle("-fx-background-radius: 5;");
        popup.getContent().add(content);

        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.show(ownerStage, ownerStage.getX() + 20, ownerStage.getY() + 20);

        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> {
            popup.hide();
            if (afterMessage != null) {
                afterMessage.run();
            }
        });
        delay.play();
    }

    public Customer findCustomer(String username, String password) {
        List<Customer> customerList = DataBase.getCustomerList();
        for (Customer customer : customerList) {
            if (customer.getUsername().equals(username) && customer.getPassword().equals(password)) {
                return customer;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}