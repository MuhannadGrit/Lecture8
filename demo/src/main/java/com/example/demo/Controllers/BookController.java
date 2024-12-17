package com.example.demo.Controllers;

import com.example.demo.Models.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private List<Book> books = new ArrayList<>();

    public BookController() {
        books.add(new Book(1, "Java Programming", "James Gosling"));
        books.add(new Book(2, "Spring in Action", "Caring Walles"));
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return books;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable int id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Book> getBookByName(@PathVariable String name) {
        return books.stream()
                .filter(book -> book.getName().equalsIgnoreCase(name))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        if (book != null && book.getId() > 0) {
            books.add(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(book);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable int id, @RequestBody Book updatedBook) {
        for (Book book : books){
            if(book.getId() == id){
                book.setName(updatedBook.getName());
                book.setAuthor(updatedBook.getAuthor());
                return ResponseEntity.ok(book);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable int id) {
        boolean removed = false;
        for (int i = 0; i < books.size(); i++) {
           if (books.get(i).getId() == id) {
               books.remove(i);
               removed = true;
           }
        }
        if(removed){
            return ResponseEntity.ok("Book with ID" + id + " has been deleted.");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with ID" + id + " NOT FOUND.");
        }
    }
}
