package com.dongha.book.web;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

class ProfileControllerUnitTest {

    @Test
    void real_profile_조회() {
        // given
        String expectedProfile = "real";
        MockEnvironment mockEnvironment = new MockEnvironment();
        mockEnvironment.addActiveProfile(expectedProfile);
        mockEnvironment.addActiveProfile("oauth");
        mockEnvironment.addActiveProfile("real-db");

        ProfileController profileController = new ProfileController(mockEnvironment);

        // when
        String profile = profileController.profile();

        // then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    void real_profile_이_없으면_첫_번째_profile_조회() {
        // given
        String expectedProfile = "oauth";
        MockEnvironment mockEnvironment = new MockEnvironment();
        mockEnvironment.addActiveProfile(expectedProfile);
        mockEnvironment.addActiveProfile("real-db");

        ProfileController profileController = new ProfileController(mockEnvironment);

        // when
        String profile = profileController.profile();

        // then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    void active_profile_이_없으면_default_조회() {
        // given
        String expectedProfile = "default";
        MockEnvironment mockEnvironment = new MockEnvironment();

        ProfileController profileController = new ProfileController(mockEnvironment);

        // when
        String profile = profileController.profile();

        // then
        assertThat(profile).isEqualTo(expectedProfile);
    }
}