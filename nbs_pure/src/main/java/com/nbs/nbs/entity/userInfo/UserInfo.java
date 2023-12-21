package com.nbs.nbs.entity.userInfo;

import com.nbs.nbs.entity.authGroupUser.AuthGroupUser;
import com.nbs.nbs.entity.refreshToken.RefreshToken;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@ToString(exclude = {"refreshTokens", "authGroupUsers"})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_userinfo")
public class UserInfo implements UserDetails {

	@Id
	@Column(name = "user_id")
	private String userId;

	@Column(name = "user_pwd")
	private String userPwd;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "department")
	private String department;

	@Column(name = "job_title")
	private String jobTitle;

	@Column(name = "cell_phone")
	private String cellPhone;

	@Column(name = "tel_phone")
	private String telPhone;

	@Column(name = "email")
	private String email;

	@Column(name = "remark")
	private String remark;

	@Column(name = "use_yn")
	private String useYn;

	@Column(name = "del_yn")
	private String delYn;

	@Column(name = "reset_pwd")
	private String resetPwd;

	@CreatedBy
	@Column(name = "created_by")
	private String createdBy;

	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@LastModifiedBy
	@Column(name = "updated_by")
	private String updatedBy;

	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "userInfo",fetch = FetchType.LAZY)
	private Set<AuthGroupUser> authGroupUsers;


	@OneToMany(mappedBy = "userInfo")
	private Set<RefreshToken> refreshTokens;

	//메뉴권한 코드 방식 변화로 메소드 선언만
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return userPwd;
	}

	@Override
	public String getUsername() {
		return userId;
	}


	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}


	// Getters and Setters
}
