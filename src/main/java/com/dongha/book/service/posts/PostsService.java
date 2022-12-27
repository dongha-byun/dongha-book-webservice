package com.dongha.book.service.posts;

import com.dongha.book.domain.posts.Posts;
import com.dongha.book.domain.posts.PostsRepository;
import com.dongha.book.web.dto.PostsListResponseDto;
import com.dongha.book.web.dto.PostsResponseDto;
import com.dongha.book.web.dto.PostsSaveRequestDto;
import com.dongha.book.web.dto.PostsUpdateRequestDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    public PostsResponseDto findById(Long id) {
        Posts posts = getPosts(id);
        return new PostsResponseDto(posts);
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = getPosts(id);
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    private Posts getPosts(Long id) {
        return postsRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
                );
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc(){
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id){
        Posts post = getPosts(id);
        postsRepository.delete(post);
    }
}
