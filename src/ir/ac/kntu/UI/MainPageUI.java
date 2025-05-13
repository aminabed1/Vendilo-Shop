package ir.ac.kntu.UI;

import ir.ac.kntu.Customer;
import ir.ac.kntu.Person;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainPageUI extends Application {

    private boolean isPopupPinned = false;
    private VBox accountPopupContent;
    private boolean isAccountPopupVisible = false;
    private Customer currentCustomer;

    public MainPageUI(Person currentCustomer) {
        this.currentCustomer = (Customer) currentCustomer;
    }

    @Override
    public void start(Stage primaryStage) {
        // Main container with gradient background
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef);");

        // Header with improved styling
        HBox header = new HBox(20);
        header.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefHeight(70);

        // Logo with better sizing
        Image logoImage = new Image("file:F:/advancedProgramming/untitled/iamges/loginpage.jpg");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitHeight(40);
        logoView.setPreserveRatio(true);

        // User icon with circular mask
        Image userImage = new Image("file:F:/advancedProgramming/untitled/iamges/user.jpg");
        ImageView userView = new ImageView(userImage);
        userView.setFitHeight(36);
        userView.setFitWidth(36);
        userView.setPreserveRatio(true);
        userView.setStyle("-fx-background-radius: 50%; -fx-border-radius: 50%;");

        // Title with better typography
        Label title = new Label("VENDILO");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        title.setFont(Font.font("Arial Rounded MT Bold", FontWeight.EXTRA_BOLD, 28));

        // Category button with better styling
        Button categoryButton = new Button("Product Categories ▼");
        categoryButton.setStyle("-fx-font-size: 16px; -fx-text-fill: #2c3e50; -fx-font-weight: bold; " +
                "-fx-background-color: transparent; -fx-padding: 8 15; -fx-border-radius: 5;");
        categoryButton.setOnMouseEntered(e -> categoryButton.setStyle("-fx-text-fill: #3498db; -fx-font-size: 16px; -fx-font-weight: bold;"));
        categoryButton.setOnMouseExited(e -> categoryButton.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 16px; -fx-font-weight: bold;"));

        // Search bar with icon
        TextField searchField = new TextField();
        searchField.setPromptText("Search products...");
        searchField.setStyle("-fx-background-color: #f1f3f5; -fx-background-radius: 20; -fx-border-radius: 20; " +
                "-fx-padding: 8 15 8 35; -fx-font-size: 14px;");
        searchField.setPrefWidth(300);

        // Search icon
        ImageView searchIcon = new ImageView(new Image("file:search_icon.png")); // You need to add a search icon
        searchIcon.setFitHeight(16);
        searchIcon.setFitWidth(16);
        StackPane searchIconWrapper = new StackPane(searchIcon);
        searchIconWrapper.setPadding(new Insets(0, 0, 0, 10));
        searchIconWrapper.setAlignment(Pos.CENTER_LEFT);

        // Search container
        StackPane searchContainer = new StackPane();
        searchContainer.getChildren().addAll(searchField, searchIconWrapper);
        StackPane.setAlignment(searchIconWrapper, Pos.CENTER_LEFT);

        // User account area
        Label account = new Label(currentCustomer.getName() + " " + currentCustomer.getSurname());
        account.setStyle("-fx-font-size:14px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");
        account.setGraphic(userView);
        account.setContentDisplay(ContentDisplay.RIGHT);
        account.setGraphicTextGap(10);
        account.setPadding(new Insets(5, 15, 5, 5));
        account.setStyle("-fx-background-color: #f1f3f5; -fx-background-radius: 20; -fx-padding: 5 15 5 5;");

        // Spacers for layout
        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        // Header layout
        header.getChildren().addAll(categoryButton, leftSpacer, searchContainer, rightSpacer, account);

        // Category popup with better styling
        VBox popupContent = new VBox(5);
        popupContent.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-padding: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        popupContent.setPrefSize(220, 180);
        popupContent.setVisible(false);

        // Add category items
        String[] categories = {"Electronics", "Clothing", "Home & Garden", "Books", "Sports", "Beauty"};
        for (String category : categories) {
            Label item = new Label(category);
            item.setStyle("-fx-font-size: 14px; -fx-padding: 8 10; -fx-text-fill: #2c3e50;");
            item.setMaxWidth(Double.MAX_VALUE);
            item.setOnMouseEntered(e -> item.setStyle("-fx-font-size: 14px; -fx-padding: 8 10; -fx-text-fill: #3498db; -fx-background-color: #f8f9fa;"));
            item.setOnMouseExited(e -> item.setStyle("-fx-font-size: 14px; -fx-padding: 8 10; -fx-text-fill: #2c3e50;"));
            popupContent.getChildren().add(item);
        }

        // Account popup with better styling
        accountPopupContent = new VBox(5);
        accountPopupContent.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-padding: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        accountPopupContent.setPrefSize(200, 160);
        accountPopupContent.setVisible(false);

        // User info header in popup
        Label userInfo = new Label(currentCustomer.getName() + "\n" + currentCustomer.getEmail());
        userInfo.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d; -fx-padding: 0 0 10 0;");
        userInfo.setMaxWidth(Double.MAX_VALUE);

        // Popup buttons with consistent styling
        Button editInfoButton = createPopupButton("Edit Profile", "#3498db");
        Button orderButton = createPopupButton("My Orders", "#2ecc71");
        Button wishlistButton = createPopupButton("Wishlist", "#9b59b6");
        Button logOutButton = createPopupButton("Log Out", "#e74c3c");

        accountPopupContent.getChildren().addAll(userInfo, editInfoButton, orderButton, wishlistButton, logOutButton);

        // Add all to root
        root.getChildren().addAll(header, popupContent, accountPopupContent);
        AnchorPane.setTopAnchor(header, 0.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);

        // Category popup behavior
        categoryButton.setOnMouseClicked(e -> {
            isPopupPinned = !isPopupPinned;
            toggleCategoryPopup(categoryButton, header, popupContent);
        });

        categoryButton.setOnMouseEntered(e -> {
            if (!isPopupPinned) {
                toggleCategoryPopup(categoryButton, header, popupContent);
            }
        });

        categoryButton.setOnMouseExited(e -> {
            if (!isPopupPinned) {
                popupContent.setVisible(false);
            }
        });

        // Account popup behavior
        account.setOnMouseClicked(e -> {
            isAccountPopupVisible = !isAccountPopupVisible;
            toggleAccountPopup(account, accountPopupContent);
        });

        account.setOnMouseEntered(e -> {
            if (!isAccountPopupVisible) {
                toggleAccountPopup(account, accountPopupContent);
            }
        });

        account.setOnMouseExited(e -> {
            if (!isAccountPopupVisible) {
                accountPopupContent.setVisible(false);
            }
        });

        // Main content area (placeholder - you can add your actual content here)
        VBox mainContent = new VBox(20);
        mainContent.setStyle("-fx-background-color: transparent; -fx-padding: 20;");
        mainContent.setAlignment(Pos.TOP_CENTER);

        Label welcomeLabel = new Label("Welcome to Vendilo, " + currentCustomer.getName() + "!");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

        Label featuredLabel = new Label("Featured Products");
        featuredLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #3498db; -fx-font-weight: bold;");

        // Add some placeholder content
        HBox productPlaceholders = new HBox(15);
        productPlaceholders.setAlignment(Pos.CENTER);
        for (int i = 0; i < 4; i++) {
            VBox productCard = createProductCard();
            productPlaceholders.getChildren().add(productCard);
        }

        mainContent.getChildren().addAll(welcomeLabel, featuredLabel, productPlaceholders);
        AnchorPane.setTopAnchor(mainContent, 80.0);
        AnchorPane.setLeftAnchor(mainContent, 0.0);
        AnchorPane.setRightAnchor(mainContent, 0.0);
        root.getChildren().add(mainContent);

        // Scene setup
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Vendilo - Online Shopping");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
    }

    private Button createPopupButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-color: " + color + "; -fx-padding: 8 15; -fx-background-radius: 5; " +
                "-fx-cursor: hand; -fx-alignment: center-left;");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 14px; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-color: derive(" + color + ", -10%); " +
                "-fx-padding: 8 15; -fx-background-radius: 5; -fx-cursor: hand;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 14px; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-background-color: " + color + "; " +
                "-fx-padding: 8 15; -fx-background-radius: 5; -fx-cursor: hand;"));
        return button;
    }

    private void toggleCategoryPopup(Button categoryButton, HBox header, VBox popupContent) {
        Bounds bounds = categoryButton.localToScene(categoryButton.getBoundsInLocal());
        double x = bounds.getMinX();
        popupContent.setLayoutX(x);
        popupContent.setLayoutY(header.getHeight() + 5);
        popupContent.setVisible(true);
    }

    private void toggleAccountPopup(Label account, VBox popupContent) {
        Bounds bounds = account.localToScene(account.getBoundsInLocal());
        popupContent.setLayoutX(bounds.getMinX() - popupContent.getPrefWidth() + account.getWidth());
        popupContent.setLayoutY(bounds.getMaxY() + 5);
        popupContent.setVisible(true);
    }

    private VBox createProductCard() {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1);");
        card.setPrefSize(200, 250);
        card.setAlignment(Pos.TOP_CENTER);

        // Product image placeholder
        Rectangle imagePlaceholder = new Rectangle(150, 150);
        imagePlaceholder.setFill(Color.LIGHTGRAY);
        imagePlaceholder.setArcWidth(10);
        imagePlaceholder.setArcHeight(10);

        // Product info
        Label name = new Label("Product Name");
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label price = new Label("$99.99");
        price.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-font-size: 16px;");

        Button addToCart = new Button("Add to Cart");
        addToCart.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 5; -fx-padding: 5 15;");

        card.getChildren().addAll(imagePlaceholder, name, price, addToCart);
        return card;
    }

    public static void main(String[] args) {
        launch(args);
    }
}