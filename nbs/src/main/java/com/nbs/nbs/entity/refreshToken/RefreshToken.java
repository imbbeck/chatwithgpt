package com.nbs.nbs.entity.refreshToken;

import com.nbs.nbs.entity.userInfo.UserInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_refreshtoken")
@EntityListeners(AuditingEntityListener.class)
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "token_idx")
	private Integer tokenIdx;

	@Column(name = "refreshtoken", nullable = false, length = 2000)
	private String refreshToken;

	@Column(name = "expired_yn", nullable = false, length = 1)
	private String expiredYn;

	@Column(name = "valid_yn", nullable = false, length = 1)
	private String validYn;

	@CreatedBy
	@Column(name = "created_by", nullable = false, length = 20)
	private String createdBy;

	@CreatedDate
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedBy
	@Column(name = "updated_by", nullable = false, length = 20)
	private String updatedBy;

	@LastModifiedDate
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private UserInfo userInfo;

	// Getters and Setters
}
