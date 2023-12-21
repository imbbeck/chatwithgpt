package com.nbs.nbs.repository;

import com.nbs.nbs.entity.buildingInfo.BuildingInfo;
import com.nbs.nbs.entity.buildingInfo.BuildingInfoRepository;
import com.nbs.nbs.entity.buildingInfo.DTO.RetrieveAllBuildingsDTO;
import com.nbs.nbs.entity.commonCode.CommonCode;
import com.nbs.nbs.entity.commonCode.CommonCodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
//@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BuildingInfoRepositoryTest extends BaseRepositoryTest<BuildingInfo,String> {
    @Autowired
    private BuildingInfoRepository buildingInfoRepository;

    @Autowired
    private CommonCodeRepository commonCodeRepository;

    @Override
    protected BuildingInfo createEntity() {
        String Id = stringId();
        BuildingInfo entity = BuildingInfo.builder()
                .buildingId(Id)
                .buildingName("app")
                .createdBy("system")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .facilityType(Id)
                .updatedBy("system")
                .build();
        return entity;
    }

    @Override
    protected String getId(BuildingInfo entity) {
        return entity.getBuildingId();
    }

    // 추가 테스트 코드

    // findDTOAllBuildingInfo 메소드 테스트, 빌딩정보를 DTO로 가져옴. CommonCode 조인 포함
    @Transactional
    @Test
    public void testFindDTOAllBuildingInfo() {
        // buildingInfo 객체 생성
        BuildingInfo buildingInfo = createEntity();

        // Common code 생성
        CommonCode commonCode = CommonCode.builder()
                .codeId(buildingInfo.getFacilityType())
                .codeName("codeName")
                .sortOdr(1)
                .useYn("Y")
                .build();

        // save 전 크기 확인
        List<RetrieveAllBuildingsDTO> beforeSaveRetrieveAllBuildingsDTOS = buildingInfoRepository.findDTOAllBuildingInfo();
        int beforeSaveRetrieveAllBuildingsDTOSSize = beforeSaveRetrieveAllBuildingsDTOS.size();

        // save
        commonCodeRepository.save(commonCode);
        buildingInfoRepository.save(buildingInfo);

        // save 후 크기 확인
        List<RetrieveAllBuildingsDTO> afterSaveRetrieveAllBuildingsDTOS = buildingInfoRepository.findDTOAllBuildingInfo();
        int afterSaveRetrieveAllBuildingsDTOSSize = afterSaveRetrieveAllBuildingsDTOS.size();


        // 차의 사이즈가 1인 것을 확인
        assertTrue(afterSaveRetrieveAllBuildingsDTOSSize -beforeSaveRetrieveAllBuildingsDTOSSize  == 1);

        // 조인이 제대로 됐는지 확인
        boolean joinResult=afterSaveRetrieveAllBuildingsDTOS.stream().anyMatch(dto -> dto.getFacilityTypeName().equals(commonCode.getCodeName()));
        assertTrue(joinResult);
    }

    // findDTOAllBuildingInfo 메소드 테스트, 빌딩정보 한개를 DTO로 가져옴. CommonCode 조인 포함
    @Transactional
    @Test
    public void testFindDTOByBuildingId() {
        // buildingInfo 객체 생성
        BuildingInfo buildingInfo = createEntity();

        // Common code 생성
        CommonCode commonCode = CommonCode.builder()
                .codeId(buildingInfo.getFacilityType())
                .codeName("codeName")
                .sortOdr(1)
                .useYn("Y")
                .build();

        // save
        commonCodeRepository.save(commonCode);
        buildingInfoRepository.save(buildingInfo);

        // 타켓 메소드 이용해서 값 가져오기
        var result = buildingInfoRepository.findDTOByBuildingId(buildingInfo.getBuildingId());

        // result가 존재하는 것을 확인
        assertTrue(result.isPresent());

        // 조인이 제대로 됐는지 확인
        assertThat(result.get().getFacilityTypeName()).isEqualTo(commonCode.getCodeName());
    }
}
