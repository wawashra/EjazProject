package com.ejazbzu.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TagMapperTest {

    private TagMapper tagMapper;

    @BeforeEach
    public void setUp() {
        tagMapper = new TagMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(tagMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(tagMapper.fromId(null)).isNull();
    }
}
