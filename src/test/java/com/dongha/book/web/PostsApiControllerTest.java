package com.dongha.book.web;

import static org.assertj.core.api.Assertions.*;

import com.dongha.book.domain.posts.Posts;
import com.dongha.book.domain.posts.PostsRepository;
import com.dongha.book.web.dto.PostsSaveRequestDto;
import com.dongha.book.web.dto.PostsUpdateRequestDto;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void after(){
        postsRepository.deleteAll();
    }

    @Test
    public void saveTest(){
        // given
        String title = "제목1";
        String content = "본문1";
        PostsSaveRequestDto saveRequest = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();
        String url = "http://localhost:" + port + "/api/v1/posts";

        // when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, saveRequest, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> posts = postsRepository.findAll();
        assertThat(posts.get(0).getTitle()).isEqualTo(title);
        assertThat(posts.get(0).getContent()).isEqualTo(content);
    }

    @Test
    public void updateTest(){
        // given
        Posts savePost = postsRepository.save(
                Posts.builder()
                        .title("제목1")
                        .content("본문1")
                        .author("저자1")
                        .build()
        );
        PostsUpdateRequestDto updateDto = PostsUpdateRequestDto.builder()
                .title("제목2")
                .content("본문2")
                .build();
        String url = "http://localhost:" + port + "/api/v1/posts/"+savePost.getId();
        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(updateDto);

        // when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> posts = postsRepository.findAll();
        assertThat(posts.get(0).getTitle()).isEqualTo("제목2");
        assertThat(posts.get(0).getContent()).isEqualTo("본문2");
    }
}