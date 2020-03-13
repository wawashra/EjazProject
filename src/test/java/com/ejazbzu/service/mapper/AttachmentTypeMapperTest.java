package com.ejazbzu.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AttachmentTypeMapperTest {

    private AttachmentTypeMapper attachmentTypeMapper;

    @BeforeEach
    public void setUp() {
        attachmentTypeMapper = new AttachmentTypeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(attachmentTypeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(attachmentTypeMapper.fromId(null)).isNull();
    }
}
