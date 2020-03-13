package com.ejazbzu.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ejazbzu.web.rest.TestUtil;

public class CollegeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(College.class);
        College college1 = new College();
        college1.setId(1L);
        College college2 = new College();
        college2.setId(college1.getId());
        assertThat(college1).isEqualTo(college2);
        college2.setId(2L);
        assertThat(college1).isNotEqualTo(college2);
        college1.setId(null);
        assertThat(college1).isNotEqualTo(college2);
    }
}
