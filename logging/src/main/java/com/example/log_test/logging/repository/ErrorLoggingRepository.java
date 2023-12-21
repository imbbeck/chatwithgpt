package com.example.log_test.logging.repository;

import com.example.log_test.logging.model.ErrorLoggingEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ErrorLoggingRepository extends JpaRepository<ErrorLoggingEntity, Long> {
}