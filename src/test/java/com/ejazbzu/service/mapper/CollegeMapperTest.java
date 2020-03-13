package com.ejazbzu.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CollegeMapperTest {

    private CollegeMapper collegeMapper;

    @BeforeEach
    public void setUp() {
        collegeMapper = new CollegeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(collegeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(collegeMapper.fromId(null)).isNull();
    }
}
