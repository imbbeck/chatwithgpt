package com.nbs.nbs.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseRepositoryTest<T, ID> {

    @Autowired
    public JpaRepository<T, ID> repository;
    private static Integer idCounter = 1;

    protected abstract T createEntity();

    public  String stringId(){
        return "" + integerId();
    }
    public synchronized Integer integerId() {
        return idCounter++;
    }

    @Test
    @Transactional
    public void testSaveAndFindById() {
        T entity = createEntity();
        T savedEntity = repository.save(entity);
        assertThat(repository.findById(getId(savedEntity))).isPresent();
    }

    @Test
    @Transactional
    public void testFindAll() {
        repository.save(createEntity());
        repository.save(createEntity());
        assertThat(repository.findAll()).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @Transactional
    public void testDelete() {
        T entity = createEntity();
        T savedEntity = repository.save(entity);
        ID id = getId(savedEntity);
        repository.deleteById(id);
        assertThat(repository.findById(id)).isNotPresent();
    }

    // 추가적인 테스트 메서드...

    protected abstract ID getId(T entity);
}
