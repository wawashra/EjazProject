package com.ejazbzu.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DocumentTypeMapperTest {

    private DocumentTypeMapper documentTypeMapper;

    @BeforeEach
    public void setUp() {
        documentTypeMapper = new DocumentTypeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(documentTypeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(documentTypeMapper.fromId(null)).isNull();
    }
}
