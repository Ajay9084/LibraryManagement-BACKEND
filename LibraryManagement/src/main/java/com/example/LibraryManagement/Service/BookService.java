package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.DTO.BookDTO;
import com.example.LibraryManagement.Entity.Book;
import com.example.LibraryManagement.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	public List<Book> getAllBooks(){
		return bookRepository.findAll();
	}

	public Book getBookById(Long id){
		Book book = bookRepository.findById(id)
				     .orElseThrow(() -> new RuntimeException("Book not found"));
	return book;
	}

	public Book addBook(BookDTO bookDTO){
		Book book = new Book();
		book.setTitle(bookDTO.getTitle());
		book.setAuthor(bookDTO.getAuthor());
		book.setIsbn(bookDTO.getIsbn());
		book.setIsAvailable(bookDTO.getIsAvailable());
		book.setQuantity(bookDTO.getQuantity());
		return bookRepository.save(book);
	}

	public Book updateBook(Long id,BookDTO bookDTO){
		Book oldbook = bookRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Book not found"));
		oldbook.setTitle(bookDTO.getTitle());
		oldbook.setAuthor(bookDTO.getAuthor());
		oldbook.setIsbn(bookDTO.getIsbn());
		oldbook.setIsAvailable(bookDTO.getIsAvailable());
		oldbook.setQuantity(bookDTO.getQuantity());
		return bookRepository.save(oldbook);
	}

	public void deleteBookById(Long id){
		bookRepository.deleteById(id);
	}
}


