package com.example.log_test.logging.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_menulog")
public class MenuLoggingEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "log_idx", nullable = false)
	private Long id;

	@Column(name = "menu_id", nullable = false, length = 200)
	private String menuId;

	@Column(name = "user_id", nullable = false, length = 20)
	private String userId;

	@Column(name = "log_content", length = 300)
	private String logContent;

	@Column(name = "created_by", nullable = false, length = 20)
	private String createdBy;

	@Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(3)")
	private Instant createdAt;

}