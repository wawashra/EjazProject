package com.ejazbzu.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ejazbzu.web.rest.TestUtil;

public class AttachmentTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttachmentType.class);
        AttachmentType attachmentType1 = new AttachmentType();
        attachmentType1.setId(1L);
        AttachmentType attachmentType2 = new AttachmentType();
        attachmentType2.setId(attachmentType1.getId());
        assertThat(attachmentType1).isEqualTo(attachmentType2);
        attachmentType2.setId(2L);
        assertThat(attachmentType1).isNotEqualTo(attachmentType2);
        attachmentType1.setId(null);
        assertThat(attachmentType1).isNotEqualTo(attachmentType2);
    }
}
