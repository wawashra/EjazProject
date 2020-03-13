package com.ejazbzu.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ejazbzu.web.rest.TestUtil;

public class CollegeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollegeDTO.class);
        CollegeDTO collegeDTO1 = new CollegeDTO();
        collegeDTO1.setId(1L);
        CollegeDTO collegeDTO2 = new CollegeDTO();
        assertThat(collegeDTO1).isNotEqualTo(collegeDTO2);
        collegeDTO2.setId(collegeDTO1.getId());
        assertThat(collegeDTO1).isEqualTo(collegeDTO2);
        collegeDTO2.setId(2L);
        assertThat(collegeDTO1).isNotEqualTo(collegeDTO2);
        collegeDTO1.setId(null);
        assertThat(collegeDTO1).isNotEqualTo(collegeDTO2);
    }
}
