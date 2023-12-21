package com.nbs.nbs.entity.commonCode;

import com.nbs.nbs.entity.commonCode.DTO.RetrieveCommonCodeDTO;
import com.nbs.nbs.entity.commonCode.DTO.RetrieveCommonCodeNameAndIdDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommonCodeRepository extends JpaRepository<CommonCode, CommonCodeId> {

    @Query("SELECT new com.nbs.nbs.entity.commonCode.DTO.RetrieveCommonCodeDTO(c.codeId,c.codeName,c.prntcodeId,c.sortOdr,c.useYn) FROM CommonCode c ")
    List<RetrieveCommonCodeDTO> findDTOAllCodes();

    @Query("SELECT new com.nbs.nbs.entity.commonCode.DTO.RetrieveCommonCodeDTO(c.codeId, c.codeName,c.prntcodeId, c.sortOdr, c.useYn) FROM CommonCode c WHERE c.prntcodeId = :code_id")
    List<RetrieveCommonCodeDTO> findDTOCodesByPrntcodeId(@Param("code_id") String code_id);

    void deleteByCodeId(String codeId);

    void deleteAllByCodeIdIn(List<String> codeIds);

    Optional<CommonCode> findByCodeIdAndPrntcodeId(String codeId, String prntcodeId);

    boolean existsByCodeIdAndPrntcodeId(String codeId, String prntcodeId);

    boolean existsByPrntcodeId(String prntcodeId);

    @Query("SELECT new com.nbs.nbs.entity.commonCode.DTO.RetrieveCommonCodeNameAndIdDTO(c.codeId,c.codeName )from CommonCode c WHERE c.prntcodeId = :prntcodeId")
    List<RetrieveCommonCodeNameAndIdDTO> findCodeNamesByPrntcodeId(@Param("prntcodeId") String prntcodeId);

}

