package com.nbs.nbs.repository;

import com.nbs.nbs.entity.commonCode.CommonCode;
import com.nbs.nbs.entity.commonCode.CommonCodeId;
import com.nbs.nbs.entity.commonCode.CommonCodeRepository;
import com.nbs.nbs.entity.commonCode.DTO.RetrieveCommonCodeDTO;
import com.nbs.nbs.entity.commonCode.DTO.RetrieveCommonCodeNameAndIdDTO;
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
public class CommonCodeRepositoryTest extends BaseRepositoryTest<CommonCode, CommonCodeId> {
    @Autowired
    private CommonCodeRepository commonCodeRepository;

    @Override
    protected CommonCode createEntity() {
        String Id = stringId();
        CommonCode entity = CommonCode.builder()
                .codeId(Id)
                //prntId는 디폴트설정 "0000000000"
                .codeName(Id)
                .sortOdr(1)
                .useYn("Y")
                .build();
        return entity;
    }

    @Override
    protected CommonCodeId getId(CommonCode entity) {
        CommonCodeId codeId = CommonCodeId.builder()
                .codeId(entity.getCodeId())
                .prntcodeId(entity.getPrntcodeId())
                .build();
        return codeId;
    }

    // 추가 테스트

    //RetrieveCommonCodeDTO로 모든 코드를 가져오는 findDTOAllCodes 테스트
    @Test
    public void testFindDTOAllCodes() {
        // 첫 번째 CommonCode 저장 및 검색
        CommonCode commonCode = createEntity();
        commonCodeRepository.save(commonCode);
        List<RetrieveCommonCodeDTO> codeDTOS = commonCodeRepository.findDTOAllCodes();
        int initialSize = codeDTOS.size();

        // 두 번째 CommonCode 저장 및 검색
        CommonCode commonCode1 = createEntity();
        commonCodeRepository.save(commonCode1);
        List<RetrieveCommonCodeDTO> codeDTOSPlusSize = commonCodeRepository.findDTOAllCodes();

        // 검증: 리스트 크기가 정확히 1만큼 증가했는지 확인
        assertTrue(codeDTOSPlusSize.size() - initialSize == 1);
    }

    // 하위코드 가져오는 코드 내 codeID를 부모코드로 하는 코드를 가져오는 메소드 테스트
    @Test
    public void testFindDTOCodesByPrntcodeId() {
        // 첫 번째 CommonCode 저장 및 검색
        CommonCode supercommonCode = createEntity();
        supercommonCode.setCodeId("testprntId");
        CommonCode subcommonCode = createEntity();
        subcommonCode.setPrntcodeId(supercommonCode.getCodeId());
        commonCodeRepository.saveAll(List.of(subcommonCode, subcommonCode));
        List<RetrieveCommonCodeDTO> resultList = commonCodeRepository.findDTOCodesByPrntcodeId(supercommonCode.getCodeId());
        boolean resultListExist = !resultList.isEmpty();
        boolean resultListAllmatch = resultList.stream().allMatch(dto -> dto.getPrntcodeId().equals(supercommonCode.getCodeId()));
        // 검증
        assertTrue(resultListExist);
        assertTrue(resultListAllmatch);
    }

    // codeId로 삭제하는 메소드 테스트
    @Transactional
    @Test
    public void testDeleteByCodeId() {
        // 초기 코드 목록 크기 저장
        List<RetrieveCommonCodeDTO> allCodesInitial = commonCodeRepository.findDTOAllCodes();
        int initialSize = allCodesInitial.size();

        // 새 CommonCode 저장 후 목록 크기 확인
        CommonCode commonCode = createEntity();
        commonCodeRepository.save(commonCode);
        List<RetrieveCommonCodeDTO> allCodesAfterSave = commonCodeRepository.findDTOAllCodes();
        int sizeAfterSave = allCodesAfterSave.size();

        // 저장한 CommonCode 삭제 후 목록 크기 확인
        commonCodeRepository.deleteByCodeId(commonCode.getCodeId());
        List<RetrieveCommonCodeDTO> allCodesAfterDelete = commonCodeRepository.findDTOAllCodes();
        int sizeAfterDelete = allCodesAfterDelete.size();

        // 저장 후 크기 증가 및 삭제 후 원래 크기로 복귀 확인
        assertTrue(sizeAfterSave - initialSize == 1 && initialSize == sizeAfterDelete);
    }

    // codeids를 리스트로 받아서 해당되는 모든 코드 삭제하기.
    @Transactional
    @Test
    public void testDeleteAllByCodeIdIn() {
        // 초기 코드 목록 크기 저장
        List<RetrieveCommonCodeDTO> allCodesInitial = commonCodeRepository.findDTOAllCodes();
        int initialSize = allCodesInitial.size();

        // 새로운 CommonCode 객체들 저장 후 목록 크기 확인
        CommonCode commonCode1 = createEntity();
        CommonCode commonCode2 = createEntity();
        CommonCode commonCode3 = createEntity();
        commonCodeRepository.saveAll(List.of(commonCode1, commonCode2, commonCode3));
        List<RetrieveCommonCodeDTO> allCodesAfterSave = commonCodeRepository.findDTOAllCodes();
        int sizeAfterSave = allCodesAfterSave.size();

        // 저장된 CommonCode 객체들을 일괄 삭제 후 목록 크기 확인
        List<String> codesToDelete = List.of(commonCode1.getCodeId(), commonCode2.getCodeId(), commonCode3.getCodeId());
        commonCodeRepository.deleteAllByCodeIdIn(codesToDelete);
        List<RetrieveCommonCodeDTO> allCodesAfterDelete = commonCodeRepository.findDTOAllCodes();
        int sizeAfterDelete = allCodesAfterDelete.size();

        // 저장 후 크기가 3만큼 증가했는지 및 삭제 후 원래 크기로 복귀했는지 확인
        assertTrue(sizeAfterSave - initialSize == 3 && initialSize == sizeAfterDelete);
    }

    // findByCodeIdAndPrntcodeId 메소드 테스트
    @Transactional
    @Test
    public void testFindByCodeIdAndPrntcodeId() {
        // 새로운 CommonCode 객체 생성
        CommonCode commonCode = createEntity();
        String codeId = commonCode.getCodeId();
        String prntId = commonCode.getPrntcodeId();

        // 저장 전 해당 ID로 조회 시도
        var beforeSaveCode = commonCodeRepository.findByCodeIdAndPrntcodeId(codeId, prntId);

        // CommonCode 객체 저장
        commonCodeRepository.save(commonCode);

        // 저장 후 해당 ID로 조회 시도
        var afterSaveCode = commonCodeRepository.findByCodeIdAndPrntcodeId(codeId, prntId);

        // 저장 전 코드가 존재하지 않음을 확인
        assertFalse(beforeSaveCode.isPresent());

        // 저장 후 코드가 존재함을 확인
        assertTrue(afterSaveCode.isPresent());
    }

    // existsByCodeIdAndPrntcodeId 메소드 테스트, code,prnt id로 존재여부 확인
    @Transactional
    @Test
    public void testExistsByCodeIdAndPrntcodeId() {
        // 새로운 CommonCode 객체 생성
        CommonCode commonCode = createEntity();
        String codeId = commonCode.getCodeId();
        String prntId = commonCode.getPrntcodeId();

        // 저장 전 해당 ID로 조회 시도
        boolean beforeSaveCodeExist = commonCodeRepository.existsByCodeIdAndPrntcodeId(codeId, prntId);

        // CommonCode 객체 저장
        commonCodeRepository.save(commonCode);

        // 저장 후 해당 ID로 조회 시도
        boolean afterSaveCodeExist = commonCodeRepository.existsByCodeIdAndPrntcodeId(codeId, prntId);

        // 저장 전 코드가 존재하지 않음을 확인
        assertFalse(beforeSaveCodeExist);

        // 저장 후 코드가 존재함을 확인
        assertTrue(afterSaveCodeExist);
    }

    // existsByPrntcodeId 메소드 테스트, 대상 codeId를 prntId로 사용하는 코드가 존재하는지 확인
    @Transactional
    @Test
    public void testExistsByPrntcodeId() {
        // 새로운 CommonCode 객체 생성
        CommonCode commonCode = createEntity();
        String codeId = "testCodeId";
        String prntId = "testPrntcodeId";
        commonCode.setCodeId(codeId);
        commonCode.setPrntcodeId(prntId);

        // 저장 전 해당 ID로 조회 시도
        boolean beforeSaveCodeExist = commonCodeRepository.existsByPrntcodeId(prntId);

        // CommonCode 객체 저장
        commonCodeRepository.save(commonCode);

        // 저장 후 해당 ID로 조회 시도
        boolean afterSaveCodeExist = commonCodeRepository.existsByPrntcodeId(prntId);

        // 저장 전 코드가 존재하지 않음을 확인
        assertFalse(beforeSaveCodeExist);

        // 저장 후 코드가 존재함을 확인
        assertTrue(afterSaveCodeExist);
    }

    // findCodeNamesByPrntcodeId 메소드 테스트, 대상 codeId를 prntId로 사용하는 코드가 존재하는지 확인
    @Transactional
    @Test
    public void testFindCodeNamesByPrntcodeId() {
        // 새로운 CommonCode 객체 생성
        CommonCode commonCode1 = createEntity();
        String code1Id = commonCode1.getCodeId();
        CommonCode commonCode2 = createEntity();
        commonCode2.setPrntcodeId(code1Id);
        CommonCode commonCode3 = createEntity();
        commonCode3.setPrntcodeId(code1Id);

        // 저장 전 해당 ID로 조회 시도
        List<RetrieveCommonCodeNameAndIdDTO> beforeSaveCode = commonCodeRepository.findCodeNamesByPrntcodeId(code1Id);

        // CommonCode 객체 저장
        commonCodeRepository.saveAll(List.of(commonCode1, commonCode2, commonCode3));

        // 저장 후 해당 ID로 조회 시도
        List<RetrieveCommonCodeNameAndIdDTO> afterSaveCode = commonCodeRepository.findCodeNamesByPrntcodeId(code1Id);

        // 저장 전 코드가 존재하지 않음을 확인
        assertTrue(beforeSaveCode.isEmpty());

        // 사이즈가 2인 것을 확인
        assertTrue(afterSaveCode.size() == 2);
    }
}
