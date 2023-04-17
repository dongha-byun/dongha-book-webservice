package com.dongha.book.web;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProfileControllerTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    @DisplayName("Profile 확인 테스트")
    void profile_인증없이_조회가능() {
        // given
        String expectedProfile = "default";

        // when
        ResponseEntity<String> response = restTemplate.getForEntity("/profile", String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedProfile);
    }
}
