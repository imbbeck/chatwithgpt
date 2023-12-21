package com.nbs.nbs.repository;

import com.nbs.nbs.entity.systemApplication.DTO.RetrieveSystemApplicationNameDTO;
import com.nbs.nbs.entity.systemApplication.SystemApplication;
import com.nbs.nbs.entity.systemApplication.SystemApplicationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SystemApplicationRepositoryTest extends BaseRepositoryTest<SystemApplication,String> {
    @Autowired
    private SystemApplicationRepository systemApplicationRepository;

    @Override
    protected SystemApplication createEntity() {
        String Id = stringId();
        SystemApplication entity = SystemApplication.builder()
                .applicationId(Id)
                .applicationName("app")
                .createdBy("system")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .updatedBy("system")
                .build();
        return entity;
    }

    @Override
    protected String getId(SystemApplication entity) {
        return entity.getApplicationId();
    }

    @Test
    public void testfindDTOByUseYn() {
        List<RetrieveSystemApplicationNameDTO> result = systemApplicationRepository.findDTOByUseYn();
        List<RetrieveSystemApplicationNameDTO> sortedResult = result.stream()
                .sorted(Comparator.comparing(RetrieveSystemApplicationNameDTO::getApplicationName))
                .collect(Collectors.toList());

        List<SystemApplication> label = systemApplicationRepository.findAll();
        List<SystemApplication> filteredAndSortedList = label.stream()
                .filter(app -> "Y".equals(app.getUseYn()))
                .sorted(Comparator.comparing(SystemApplication::getApplicationName))
                .collect(Collectors.toList());


        assertThat(result).isNotEmpty();
        assertThat(sortedResult).hasSameSizeAs(filteredAndSortedList);

        for (int i = 0; i < sortedResult.size(); i++) {
            String nameFromDTOList = sortedResult.get(i).getApplicationName();
            String nameFromEntityList = filteredAndSortedList.get(i).getApplicationName();

            // 어플리케이션 이름이 같은지 확인
            assertThat(nameFromDTOList).isEqualTo(nameFromEntityList);
        }
    }
}
