package com.nbs.nbs.entity.systemApplication;

import com.nbs.nbs.entity.systemApplication.DTO.RetrieveSystemApplicationNameDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SystemApplicationRepository extends JpaRepository<SystemApplication,String> {

    @Query("SELECT new com.nbs.nbs.entity.systemApplication.DTO.RetrieveSystemApplicationNameDTO(s.applicationId,s.applicationName) FROM SystemApplication s WHERE upper(s.useYn) = 'Y'")
    List<RetrieveSystemApplicationNameDTO> findDTOByUseYn();
}
