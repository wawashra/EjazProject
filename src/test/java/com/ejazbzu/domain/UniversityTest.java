package com.ejazbzu.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ejazbzu.web.rest.TestUtil;

public class UniversityTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(University.class);
        University university1 = new University();
        university1.setId(1L);
        University university2 = new University();
        university2.setId(university1.getId());
        assertThat(university1).isEqualTo(university2);
        university2.setId(2L);
        assertThat(university1).isNotEqualTo(university2);
        university1.setId(null);
        assertThat(university1).isNotEqualTo(university2);
    }
}
