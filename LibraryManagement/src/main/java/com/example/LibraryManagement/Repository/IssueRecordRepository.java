package com.example.LibraryManagement.Repository;

import com.example.LibraryManagement.Entity.IssueRecord;
import com.example.LibraryManagement.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long> {
	List<IssueRecord> findByUser(User user);
}
