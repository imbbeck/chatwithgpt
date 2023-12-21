package com.nbs.nbs.services.common.NBSZA010_CommonCodeManagement;

import com.nbs.nbs.entity.commonCode.CommonCode;
import com.nbs.nbs.entity.commonCode.CommonCodeRepository;
import com.nbs.nbs.entity.commonCode.DTO.RegisterCommonCodeDTO;
import com.nbs.nbs.entity.commonCode.DTO.RetrieveCommonCodeDTO;
import com.nbs.nbs.exception.DataNotFoundException;
import com.nbs.nbs.exception.IdAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonCodeManagementService {
    private final CommonCodeRepository commonCodeRepository;
    private final String SuperCodePrntcodeId = "0000000000";
    private final String typeSub = "sublevel";
    private final String typeSame = "samelevel";

    public List<RetrieveCommonCodeDTO> retrieveAllCodes() {
        return commonCodeRepository.findDTOAllCodes();
    }

    public List<RetrieveCommonCodeDTO> retrieveCodesBySuperCodeID(String superCodeId) {
        return commonCodeRepository.findDTOCodesByPrntcodeId(superCodeId);
    }

    public void updateCommonCode(String codeId, String prntcodeId, RegisterCommonCodeDTO request) {
        var existingCommonCode = commonCodeRepository.findByCodeIdAndPrntcodeId(codeId, prntcodeId).orElseThrow(() -> new DataNotFoundException("No code found"));
        BeanUtils.copyProperties(request, existingCommonCode, "codeId", "prntcodeId");
        commonCodeRepository.save(existingCommonCode);
    }

    public void createCode(RegisterCommonCodeDTO request, String codeId,String prntcodeId, String type) {
        if (!type.equals(typeSub) && !type.equals(typeSame)){
            throw new RuntimeException("올바른 type을 입력하세요");
        }
        isExistsCode(request.getCodeId(), request.getPrntcodeId());
        CommonCode code = new CommonCode();
        if (type.equals(typeSub)){
            if (!request.getPrntcodeId().equals(SuperCodePrntcodeId) || !prntcodeId.equals(SuperCodePrntcodeId)){
                throw new RuntimeException("하위 코드는 하위 코드를 생성할 수 없습니다.");
            }
            BeanUtils.copyProperties(request, code, "prntcodeId");
            code.setPrntcodeId(codeId);
        }
        if (type.equals(typeSame)){
            BeanUtils.copyProperties(request, code, "prntcodeId");
            code.setPrntcodeId(prntcodeId);
        }
        if (code.getCodeId() != null) {
            commonCodeRepository.save(code);
        }
    }

    private void isExistsCode(String coedId, String prntcodeId) {
        boolean exists = commonCodeRepository.existsByCodeIdAndPrntcodeId(coedId, prntcodeId);
        if (exists) {
            throw new IdAlreadyExistsException("The ID is already in use.");
        }
    }

    public void deleteSuperCode(String supercodeId) {
        boolean isPresentSubCode = commonCodeRepository.existsByPrntcodeId(supercodeId);
        if (isPresentSubCode) {
            throw new RuntimeException("코드에 대한 하위코드가 존재합니다.");
        }
        commonCodeRepository.deleteByCodeId(supercodeId);
    }

    public void deleteSubCodes(List<String> codeIds) {
        for (String codeId : codeIds) {
            boolean superCode = commonCodeRepository.existsByCodeIdAndPrntcodeId(codeId, SuperCodePrntcodeId);
            if (superCode) {
                throw new RuntimeException("상위 코드가 포함 되어 있습니다.");
            }
            commonCodeRepository.deleteAllByCodeIdIn(codeIds);
        }
    }
}
