package com.example.log_test.dbmanage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_dbmanagelog")
public class DBmanageLoggingEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_idx", nullable = false)
	private Long id;

	@Column(name = "process_type", nullable = false, length = 20)
	private String processType;

	@Column(name = "file_name", length = 30)
	private String fileName;

	@Column(name = "elapsed_time")
	private Double elapsedTime;

	@Column(name = "response", length = 10)
	private String response;

	@Column(name = "response_detail", length = 4000)
	private String responseDetail;

	@Column(name = "created_by", nullable = false, length = 20)
	private String createdBy;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

}