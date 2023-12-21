package com.nbs.nbs.entity.logging;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ErrorLoggingRepository extends JpaRepository<ErrorLoggingEntity, Long> {
}