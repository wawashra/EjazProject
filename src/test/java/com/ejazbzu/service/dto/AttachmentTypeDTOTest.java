package com.ejazbzu.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ejazbzu.web.rest.TestUtil;

public class AttachmentTypeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttachmentTypeDTO.class);
        AttachmentTypeDTO attachmentTypeDTO1 = new AttachmentTypeDTO();
        attachmentTypeDTO1.setId(1L);
        AttachmentTypeDTO attachmentTypeDTO2 = new AttachmentTypeDTO();
        assertThat(attachmentTypeDTO1).isNotEqualTo(attachmentTypeDTO2);
        attachmentTypeDTO2.setId(attachmentTypeDTO1.getId());
        assertThat(attachmentTypeDTO1).isEqualTo(attachmentTypeDTO2);
        attachmentTypeDTO2.setId(2L);
        assertThat(attachmentTypeDTO1).isNotEqualTo(attachmentTypeDTO2);
        attachmentTypeDTO1.setId(null);
        assertThat(attachmentTypeDTO1).isNotEqualTo(attachmentTypeDTO2);
    }
}
