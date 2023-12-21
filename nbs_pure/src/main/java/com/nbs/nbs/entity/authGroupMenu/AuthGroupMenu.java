package com.nbs.nbs.entity.authGroupMenu;

import com.nbs.nbs.entity.authGroup.AuthGroup;
import com.nbs.nbs.entity.menu.Menu;
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

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@IdClass(AuthGroupMenuId.class)
@Table(name = "tb_authgrp_menu")
public class AuthGroupMenu {

	@Id
	@Column(name = "authgroup_id")
	private String authGroupId;

	@Id
	@Column(name = "menu_id")
	private String menuId;

	@Column(name = "auth_crud")
	private int authCrud;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("authGroupId")
	@JoinColumn(name = "authgroup_id", insertable = false, updatable = false)
	private AuthGroup authGroup;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("menuId") // 복합키를 사용하는 엔티티에는 MapsId를 사용한다.
	@JoinColumn(name = "menu_id", insertable = false, updatable = false)
	private Menu menu;

	// Getters and Setters

}
