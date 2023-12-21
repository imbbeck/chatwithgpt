package com.nbs.nbs.repository;

import com.nbs.nbs.entity.authGroup.AuthGroup;
import com.nbs.nbs.entity.authGroup.AuthGroupRepository;
import com.nbs.nbs.entity.authGroup.DTO.RetrieveAuthGroupsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
//@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AuthGroupRepositoryTest extends BaseRepositoryTest<AuthGroup, String> {
    @Autowired
    private AuthGroupRepository authGroupRepository;

    @Override
    protected AuthGroup createEntity() {
        String Id = stringId();
        AuthGroup entity = AuthGroup.builder()
                .authGroupId(Id)
                .authGroupName(Id)
                .useYn("Y")
                .build();
        return entity;
    }

    @Override
    protected String getId(AuthGroup entity) {
        return entity.getAuthGroupId();
    }

    // findDTOAll 메소드 테스트,authgroup을 DTO 형태로 조회함
    @Transactional
    @Test
    public void testFindDTOAll() {

        // authgroup 객체 생성
        AuthGroup authGroup = createEntity();

        // 저장전에 List 불러오기
        List<RetrieveAuthGroupsDTO> beforeSaveList = authGroupRepository.findDTOAll();

        // 저장전 List 사이즈 담아두기
        int beforeSaveListSize = beforeSaveList.size();

        // 저장
        authGroupRepository.save(authGroup);

        // 저장후에 List 불러오기
        List<RetrieveAuthGroupsDTO> afterSaveList = authGroupRepository.findDTOAll();

        // 저장후에 List 사이즈 담아두기
        int afterSaveListSize = afterSaveList.size();

        // 저장후 list 사이즈 증가했는지 확인
        assertTrue(afterSaveListSize - beforeSaveListSize == 1);
    }

    // findDTOAuthGroupById 메소드 테스트, authGroupId와 사용가능여부로 개별 authGroup을 DTO로 조회함
    @Transactional
    @Test
    public void testFindDTOAuthGroupById() {

        // authgroup1 객체 생성
        AuthGroup authGroup1UseY = createEntity();
        authGroup1UseY.setUseYn("Y");

        // authgroup2 객체 생성
        AuthGroup authGroup2UseN = createEntity();
        authGroup2UseN.setUseYn("N");

        // 저장
        authGroupRepository.saveAll(List.of(authGroup1UseY,authGroup2UseN));

        // 저장 여부 확인용 불러오기
        var authGroup1 = authGroupRepository.findById(authGroup1UseY.getAuthGroupId());
        var authGroup2 = authGroupRepository.findById(authGroup2UseN.getAuthGroupId());

        // DTO각각 불러오기
        var authGroup1DTO = authGroupRepository.findDTOAuthGroupById(authGroup1UseY.getAuthGroupId());
        var authGroup2DTO = authGroupRepository.findDTOAuthGroupById(authGroup2UseN.getAuthGroupId());

        // authGroup1 존재 확인 및 grouId 확인
        assertTrue(authGroup1.isPresent() && authGroup1DTO.isPresent() && authGroup1DTO.get().getAuthGroupId().equals(authGroup1UseY.getAuthGroupId()));

        // authGroup2 존재 확인 authGroup2DTO는 useYN가 N이므로 없어야함.
        assertFalse(authGroup2.isPresent() && authGroup2DTO.isPresent());
    }

}
