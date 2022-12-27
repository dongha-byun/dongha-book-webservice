package com.dongha.book.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dongha.book.domain.posts.Posts;
import com.dongha.book.domain.posts.PostsRepository;
import com.dongha.book.web.dto.PostsSaveRequestDto;
import com.dongha.book.web.dto.PostsUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void beforeEach(){
        this.mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void after(){
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void saveTest() throws Exception {
        // given
        String title = "제목1";
        String content = "본문1";
        PostsSaveRequestDto saveRequest = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author("author")
                .build();

        // when
        mvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(saveRequest)))
                .andExpect(status().isOk());

        // then
        List<Posts> posts = postsRepository.findAll();
        assertThat(posts.get(0).getTitle()).isEqualTo(title);
        assertThat(posts.get(0).getContent()).isEqualTo(content);

    }

    @Test
    @WithMockUser(roles = "USER")
    public void updateTest() throws Exception {
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

        // when
        mvc.perform(put("/api/v1/posts/{id}", savePost.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());

        // then
        List<Posts> posts = postsRepository.findAll();
        assertThat(posts.get(0).getTitle()).isEqualTo("제목2");
        assertThat(posts.get(0).getContent()).isEqualTo("본문2");
    }
}