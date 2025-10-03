package cohort_65.java.forumservice.post.controller;

import cohort_65.java.forumservice.post.dto.DatePeriodDto;
import cohort_65.java.forumservice.post.dto.NewCommentDto;
import cohort_65.java.forumservice.post.dto.NewPostDto;
import cohort_65.java.forumservice.post.dto.PostDto;
import cohort_65.java.forumservice.post.service.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

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

    @GetMapping("/post/{id}")
    public PostDto getPostById(@PathVariable String id) {
        return postService.getPostById(id);
    }


    @PutMapping("/post/{id}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likePost(@PathVariable String id) {
        postService.likePost(id);
    }

    @DeleteMapping("/post/{id}")
    public PostDto deletePost(@PathVariable String id) {
        return postService.deletePostById(id);
    }

    @PutMapping("/post/{id}")
    public PostDto updatePost(@PathVariable String id, @RequestBody NewPostDto newPostDto) {
        return postService.updatePostById(newPostDto, id);
    }

    @PutMapping("/post/{id}/comment/{user}")
    public PostDto addComment(@PathVariable String id,
                              @PathVariable String user, @RequestBody NewCommentDto newCommentDto) {
        return postService.addComment(id, user, newCommentDto);
    }

    @GetMapping("/posts/author/{author}")
    public Iterable<PostDto> getPostsByAuthor(@PathVariable String author) {
        return postService.getPostsByAuthor(author);
    }

    @PostMapping("/posts/tags")
    public Iterable<PostDto> getPostsByTags(@RequestBody Set<String> tags) {
        return postService.getPostsByTags(tags);
    }

    @PostMapping("/posts/period")
    public Iterable<PostDto> getPostsByPeriod(@RequestBody DatePeriodDto datePeriodDto) {
        return postService.getPostsByPeriod(datePeriodDto);
    }

    @GetMapping("/post/title/{title}")
    public Iterable<PostDto> getPostByTitle(@PathVariable String title) {
        return postService.getPostByTitle(title);
    }
}