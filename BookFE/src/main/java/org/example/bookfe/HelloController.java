package org.example.bookfe;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HelloController {

    @FXML
    private TextField input_booId;

    @FXML
    private TextField input_bookAuthor;

    @FXML
    private TextField input_bookName;

    @FXML
    private TextArea textArea_allBooks;

    @FXML
    void addNewBook(ActionEvent event) {
        try {
            int bookId = Integer.parseInt(input_booId.getText());
            String bookName = input_bookName.getText();
            String author = input_bookAuthor.getText();

            Book myBook = new Book(bookId, bookName, author);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(myBook);

            URL url = new URL("http://localhost:8090/api/books");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }

            String response = readResponse(connection);
            textArea_allBooks.setText(response);
        } catch (Exception e) {
            textArea_allBooks.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /*
    @FXML
    void addNewBook(ActionEvent event) {
        try{
            String bookName = input_bookName.getText();
            String author = input_bookAuthor.getText();
            int bookId = Integer.parseInt(input_booId.getText());

            if (bookName.isEmpty() || author.isEmpty()) {
                textArea_allBooks.setText("Please fill in both the book Name and Author");
                return;
            }
            URL url = new URL("http://localhost:8090/api/books");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            String bookJson = String.format("{\"id\":\"%s\",\"name\":\"%s\",\"author\":\"%s\"}",bookId, bookName, author);
            try(OutputStream os = connection.getOutputStream()) {
                os.write(bookJson.getBytes());
                os.flush();
            }
            String response = readResponse(connection);
            textArea_allBooks.setText(response);

        }catch (Exception e){
        textArea_allBooks.setText("Error" + e.getMessage());
        }
    }
     */

    @FXML
    void getAllBooks() {
        try {
            URL url = new URL("http://localhost:8090/api/books");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            String response = readResponse(connection);
            textArea_allBooks.setText(response);

        } catch (Exception e) {
            textArea_allBooks.setText("Error" + e.getMessage());
        }
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        BufferedReader reader;
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 300) {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }
}