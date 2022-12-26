package com.dongha.book.domain.posts;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void after(){
        postsRepository.deleteAll();
    }

    @Test
    public void save(){
        // given
        String title = "타이틀1";
        String content = "본문1";
        String author = "글쓴이";

        Posts posts = Posts.builder()
                .title(title)
                .content(content)
                .author(author).build();
        // when
        Posts savePost = postsRepository.save(posts);

        // then
        assertThat(savePost.getId()).isNotNull();
        assertThat(savePost.getTitle()).isEqualTo(title);
        assertThat(savePost.getTitle()).isEqualTo(title);
        assertThat(savePost.getTitle()).isEqualTo(title);
    }

    @Test
    public void findAll(){
        // given
        Posts post1 = Posts.builder()
                .title("제목1")
                .content("본문1")
                .author("저자1").build();
        Posts post2 = Posts.builder()
                .title("제목2")
                .content("본문2")
                .author("저자2").build();
        postsRepository.save(post1);
        postsRepository.save(post2);

        // when
        List<Posts> posts = postsRepository.findAll();

        // then
        assertThat(posts.get(0)).isNotNull();
        assertThat(posts.get(0).getContent()).isEqualTo("본문1");
        assertThat(posts.get(0).getTitle()).isEqualTo("제목1");
        assertThat(posts.get(0).getAuthor()).isEqualTo("저자1");
    }

    @Test
    public void baseEntity_등록(){
        // given
        LocalDateTime now = LocalDateTime.of(2022, 12, 26, 19, 35, 00);
        Posts post = Posts.builder()
                .title("제목1")
                .content("본문1")
                .author("저자1")
                .build();

        // when
        Posts savedPosts = postsRepository.save(post);

        System.out.println("createdDate = " + savedPosts.getCreatedDate());
        System.out.println("modifiedDate = " + savedPosts.getModifiedDate());

        // then
        assertThat(savedPosts.getCreatedDate()).isAfter(now);
        assertThat(savedPosts.getModifiedDate()).isAfter(now);
    }
}