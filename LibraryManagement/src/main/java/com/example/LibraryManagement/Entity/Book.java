package com.example.LibraryManagement.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import jakarta.persistence.Id;

@Entity
@Data
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String author;
	private String isbn;
	private Integer quantity;
	private Boolean isAvailable;
}
