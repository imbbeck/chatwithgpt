package com.nbs.nbs.entity.menu;

import com.nbs.nbs.entity.authGroupMenu.AuthGroupMenu;
import com.nbs.nbs.entity.systemApplication.SystemApplication;
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
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_menu")
public class Menu {

    @Id
    @Column(name = "menu_id")
    private String menuId;

    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @Column(name = "sort_odr", nullable = false)
    private int sortOdr;

    @Column(name = "prntmenu_id")
    private String prntmenuId;

    @Column(name = "menu_url")
    private String menuUrl;

    @Column(name = "menu_icon")
    private String menuIcon;

    @Column(name = "remark") // 이름 변경
    private String remark;

    @Column(name = "use_yn", nullable = false)
    private String useYn;

    @CreatedBy
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", referencedColumnName = "application_id", nullable = false)
    private SystemApplication systemApplication;

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    private Set<AuthGroupMenu> authGroupMenus;

    // Getter와 Setter
}
