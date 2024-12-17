module org.example.bookfe {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind; // Ensure you're importing the right module

    exports org.example.bookfe;
    opens org.example.bookfe to javafx.fxml;
}