package com.nbs.nbs.entity.userInfo;

import com.nbs.nbs.entity.userInfo.DTO.RetrieveOneUserInfoDetails;
import com.nbs.nbs.entity.userInfo.DTO.UserAuthDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo,String> {

    @Query("SELECT new com.nbs.nbs.entity.userInfo.DTO.RetrieveOneUserInfoDetails(" +
            "u.userId, u.userName, u.department, u.jobTitle ,c.codeName, " + // jobTitle 대신 c.codeName 사용
            "u.cellPhone, u.telPhone, u.email, u.remark, u.useYn, " +
            "u.createdBy, u.createdAt, u.updatedBy, u.updatedAt) " +
            "FROM UserInfo u LEFT JOIN CommonCode c ON u.jobTitle = c.codeId ")
    List<RetrieveOneUserInfoDetails> findDTOUsers();

    @Query("SELECT new com.nbs.nbs.entity.userInfo.DTO.RetrieveOneUserInfoDetails(" +
            "u.userId, u.userName, u.department, u.jobTitle, c.codeName, " + // 순서 수정
            "u.cellPhone, u.telPhone, u.email, u.remark, u.useYn, " +
            "u.createdBy, u.createdAt, u.updatedBy, u.updatedAt) " +
            "FROM UserInfo u LEFT JOIN CommonCode c ON u.jobTitle = c.codeId " +
            "WHERE u.userId = :userId")
    Optional<RetrieveOneUserInfoDetails> findDetailsByUserId(@Param("userId") String userId);

    @Query("SELECT new com.nbs.nbs.entity.userInfo.DTO.UserAuthDTO(" +
            "u.userId, u.userName, u.department, u.jobTitle, c.codeName) " + // 순서 수정
            "FROM UserInfo u JOIN u.authGroupUsers a LEFT JOIN CommonCode c ON u.jobTitle = c.codeId " +
            "WHERE a.authGroupId = :authGroupId")
    List<UserAuthDTO> findDTOUsersByAuthGroupId(@Param("authGroupId") String authGroupId);

    @Query("SELECT new com.nbs.nbs.entity.userInfo.DTO.UserAuthDTO(" +
            "u.userId, u.userName, u.department,u.jobTitle, c.codeName) " + // jobTitle 대신 c.codeName 사용
            "FROM UserInfo u LEFT JOIN CommonCode c ON u.jobTitle = c.codeId " +
            "WHERE u.userId NOT IN (SELECT a.userId FROM AuthGroupUser a WHERE a.authGroupId = :authGroupId)")
    List<UserAuthDTO> findDTOUsersByAuthGroupIdWhereNotIn(@Param("authGroupId") String authGroupId);
}
