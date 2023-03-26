package com.dongha.book.web;

import com.dongha.book.config.auth.LoginUser;
import com.dongha.book.config.auth.dto.SessionUser;
import com.dongha.book.service.posts.PostsService;
import com.dongha.book.web.dto.PostsListResponseDto;
import com.dongha.book.web.dto.PostsResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(@LoginUser SessionUser user, Model model){
        List<PostsListResponseDto> posts = postsService.findAllDesc();
        model.addAttribute("posts", posts);

        if(user != null){
            model.addAttribute("userName", user.getName());
        }

        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable("id") Long id,
                              Model model){
        PostsResponseDto post = postsService.findById(id);
        model.addAttribute("post", post);
        return "posts-update";
    }

    @GetMapping
    public String reports() {
        return "reports";
    }
}
