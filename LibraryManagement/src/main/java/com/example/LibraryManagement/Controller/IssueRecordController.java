package com.example.LibraryManagement.Controller;

import com.example.LibraryManagement.Entity.IssueRecord;
import com.example.LibraryManagement.Entity.User;
import com.example.LibraryManagement.Repository.IssueRecordRepository;
import com.example.LibraryManagement.Repository.UserRepository;
import com.example.LibraryManagement.Service.IssueRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issuerecords")
public class IssueRecordController {

	@Autowired
	private IssueRecordService issueRecordService;

	@Autowired
	private IssueRecordRepository issueRecordRepository;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/issuethebook/{bookId}")
	public ResponseEntity<IssueRecord> issueTheBook(@PathVariable(name = "bookId") Long bookId ){
    return ResponseEntity.ok(issueRecordService.issueTheBook(bookId));
	}

	@PostMapping("/returnthebook/{issueRecordId}")
	public ResponseEntity<IssueRecord> returnTheBook(@PathVariable Long issueRecordId){
	return ResponseEntity.ok(issueRecordService.returnTheBook(issueRecordId));
	}

	@GetMapping("/myissues")
	public ResponseEntity<List<IssueRecord>> getMyIssuedBooks(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));
		return ResponseEntity.ok(issueRecordRepository.findByUser(user));
	}

	@GetMapping("/getallissues")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<IssueRecord>> getAllIssueRecords(){
		return ResponseEntity.ok(issueRecordRepository.findAll());
	}

}
