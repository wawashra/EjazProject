package com.ejazbzu.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ejazbzu.web.rest.TestUtil;

public class UniversityDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UniversityDTO.class);
        UniversityDTO universityDTO1 = new UniversityDTO();
        universityDTO1.setId(1L);
        UniversityDTO universityDTO2 = new UniversityDTO();
        assertThat(universityDTO1).isNotEqualTo(universityDTO2);
        universityDTO2.setId(universityDTO1.getId());
        assertThat(universityDTO1).isEqualTo(universityDTO2);
        universityDTO2.setId(2L);
        assertThat(universityDTO1).isNotEqualTo(universityDTO2);
        universityDTO1.setId(null);
        assertThat(universityDTO1).isNotEqualTo(universityDTO2);
    }
}
