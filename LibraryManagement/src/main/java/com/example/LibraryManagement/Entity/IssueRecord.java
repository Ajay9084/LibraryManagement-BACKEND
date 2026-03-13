package com.example.LibraryManagement.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "issuerecords")
public class IssueRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonProperty("issuedAt")
	private LocalDate issueDate;

	private LocalDate dueDate;

	@JsonProperty("returnedAt")
	private LocalDate returnDate;

	@JsonProperty("isReturned")
	private Boolean returned;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;
}