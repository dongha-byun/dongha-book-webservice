package com.dongha.book.web.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class HelloResponseDtoTest {

    @Test
    public void 롬복_test(){
        // given
        String name = "test";
        int amount = 1000;

        // when
        HelloResponseDto helloResponseDto = new HelloResponseDto(name, amount);

        // then
        assertThat(helloResponseDto.getName()).isEqualTo(name);
        assertThat(helloResponseDto.getAmount()).isEqualTo(amount);
    }
}