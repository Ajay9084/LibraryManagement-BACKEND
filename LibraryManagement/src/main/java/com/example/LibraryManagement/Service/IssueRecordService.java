package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Entity.Book;
import com.example.LibraryManagement.Entity.IssueRecord;
import com.example.LibraryManagement.Entity.User;
import com.example.LibraryManagement.Repository.BookRepository;
import com.example.LibraryManagement.Repository.IssueRecordRepository;
import com.example.LibraryManagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class IssueRecordService {

	@Autowired
	private IssueRecordRepository issueRecordRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserRepository userRepository;

	public IssueRecord issueTheBook(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new RuntimeException("Book Not Found"));

		if (book.getQuantity() <= 0 || !Boolean.TRUE.equals(book.getIsAvailable())) {
			throw new RuntimeException("Book is Not Available");
		}

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not Found"));

		IssueRecord issueRecord = new IssueRecord();
		issueRecord.setIssueDate(LocalDate.now());
		issueRecord.setDueDate(LocalDate.now().plusDays(14));
		issueRecord.setReturned(false);       // ← updated
		issueRecord.setUser(user);
		issueRecord.setBook(book);

		book.setQuantity(book.getQuantity() - 1);
		if (book.getQuantity() == 0) {
			book.setIsAvailable(false);
		}

		bookRepository.save(book);
		return issueRecordRepository.save(issueRecord);
	}

	public IssueRecord returnTheBook(Long issueRecordId) {
		IssueRecord issueRecord = issueRecordRepository.findById(issueRecordId)
				.orElseThrow(() -> new RuntimeException("Issue Record not found"));

		if (issueRecord.getReturned()) {      // ← updated
			throw new RuntimeException("Book is already returned");
		}

		Book book = issueRecord.getBook();
		book.setQuantity(book.getQuantity() + 1);
		book.setIsAvailable(true);
		bookRepository.save(book);

		issueRecord.setReturnDate(LocalDate.now());
		issueRecord.setReturned(true);        // ← updated

		return issueRecordRepository.save(issueRecord);
	}
}
