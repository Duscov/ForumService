package cohort_65.java.forumservice.post.controller;

import cohort_65.java.forumservice.post.dto.NewPostDto;
import cohort_65.java.forumservice.post.dto.PostDto;
import cohort_65.java.forumservice.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forum")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post/{author}")
    public PostDto addNewPost(@RequestBody NewPostDto newPostDto,
                              @PathVariable String author) {
        return postService.addNewPost(newPostDto, author);
    }
}
