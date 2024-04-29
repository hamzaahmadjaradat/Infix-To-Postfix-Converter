module com.example.prefixtopostfixconverter {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.infixtopostfixconverter to javafx.fxml;
    exports com.example.infixtopostfixconverter;
}