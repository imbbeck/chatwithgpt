package com.nbs.nbs.repository;

import com.nbs.nbs.entity.authGroup.AuthGroup;
import com.nbs.nbs.entity.authGroup.AuthGroupRepository;
import com.nbs.nbs.entity.authGroupUser.AuthGroupUser;
import com.nbs.nbs.entity.authGroupUser.AuthGroupUserRepository;
import com.nbs.nbs.entity.userInfo.DTO.RetrieveOneUserInfoDetails;
import com.nbs.nbs.entity.userInfo.DTO.UserAuthDTO;
import com.nbs.nbs.entity.userInfo.UserInfo;
import com.nbs.nbs.entity.userInfo.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserInfoRepositoryTest extends BaseRepositoryTest<UserInfo, String> {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private AuthGroupUserRepository authGroupUserRepository;

    @Autowired
    private AuthGroupRepository authGroupRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    protected UserInfo createEntity() {
        String Id = stringId();
        UserInfo entity = UserInfo.builder()
                .userId(Id)
                .userPwd(passwordEncoder.encode("asd"))
                .jobTitle("A010001")
                .build();
        return entity;
    }

    @Override
    protected String getId(UserInfo entity) {
        return entity.getUserId();
    }
// 추가 테스트 케이스 아래에 작성

    @Test
    public void testFindDTOUsers() {
        // 필요한 경우 테스트 데이터 생성 및 저장
        UserInfo userInfo1 = createEntity();
        UserInfo userInfo2 = createEntity();

        userInfoRepository.save(userInfo1);
        userInfoRepository.save(userInfo2);
        userInfoRepository.flush();

        List<RetrieveOneUserInfoDetails> result = userInfoRepository.findDTOUsers();
        assertThat(result).isNotNull();
        assertThat(result).hasSizeGreaterThanOrEqualTo(1); // 최소한 하나 이상의 결과가 있어야 함
        result.forEach(dto -> {
            assertThat(dto.getUserId()).isNotNull();
            // 추가적인 필드 검증
        });
    }

    @Test
    public void testFindDetailsByUserId() {
        // 테스트 데이터 생성 및 저장
        UserInfo userInfo = createEntity();
        String userId = userInfo.getUserId();
        userInfoRepository.save(userInfo);
        Optional<RetrieveOneUserInfoDetails> result = userInfoRepository.findDetailsByUserId(userInfo.getUserId());
        assertThat(result).isPresent();
        result.ifPresent(dto -> {
            assertThat(dto.getUserId()).isEqualTo(userId);
            // 추가적인 필드 검증
        });
    }

    @Test
    public void testFindDTOUsersByAuthGroupId() {
        // user생성
        UserInfo userInfo = createEntity();

        // user생성 저장
        userInfoRepository.save(userInfo);

        //authgroup생성
        AuthGroup authGroup = AuthGroup.builder()
                .authGroupId("testAuthGroupId")
                .authGroupName("testAuthGroupName")
                .build();
        //authgroup저장
        authGroupRepository.save(authGroup);

        // authGroupUser생성
        AuthGroupUser authGroupUser = AuthGroupUser.builder()
                .authGroupId(authGroup.getAuthGroupId())
                .userId(userInfo.getUserId())
                .userInfo(userInfo)
                .authGroup(authGroup)
                .build();
        // authGroupUser생성
        authGroupUserRepository.save(authGroupUser);

        List<UserAuthDTO> result = userInfoRepository.findDTOUsersByAuthGroupId(authGroup.getAuthGroupId());
        assertThat(result).isNotEmpty();
        result.forEach(dto -> {
            assertThat(dto.getUserId()).isEqualTo(userInfo.getUserId());
            assertThat(dto.getJobTitleName()).isEqualTo("사원");
            // 추가적인 필드 검증
        });
    }


    @Test
    public void testFindDTOUsersByAuthGroupIdWhereNotIn() {
        String userId = "Admin";
        String authGroupId = "ADMIN";

        List<UserAuthDTO> result = userInfoRepository.findDTOUsersByAuthGroupIdWhereNotIn(authGroupId);
        assertThat(result).isNotEmpty();
        result.forEach(dto -> {
            assertThat(dto.getUserId()).isNotEqualTo(userId);
            // 추가적인 필드 검증
        });
    }

}
