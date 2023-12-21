package com.example.log_test.logging.repository;

import com.example.log_test.logging.model.MenuLoggingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuLoggingRepository extends JpaRepository<MenuLoggingEntity, Long> {
}