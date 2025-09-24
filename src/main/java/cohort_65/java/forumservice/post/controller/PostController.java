package cohort_65.java.forumservice.post.controller;

import cohort_65.java.forumservice.post.dto.NewPostDto;
import cohort_65.java.forumservice.post.dto.PostDto;
import cohort_65.java.forumservice.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
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

    @GetMapping("/post/id/{id}")
    public PostDto findPostById(@PathVariable String id) {
        return postService.findPostById(id);
    }

    @PatchMapping("/post/{id}/like")
    public void addLike(@PathVariable String id) {
        postService.addLike(id);
    }

    @GetMapping("/post/author/{author}")
    public List<PostDto> findPostsByAuthor(@PathVariable String author) {
        return postService.findPostsByAuthor(author);
    }

    @PostMapping("/post/{id}/comment")
    public void addComment(@PathVariable String id, @RequestParam String user, @RequestParam String message) {
        postService.addComment(id, user, message);
    }

    @DeleteMapping("/post/{id}")
    public void deletePost(@PathVariable String id) {
        postService.deletePost(id);
    }

    @GetMapping("/post/tags")
    public List<PostDto> findPostsByTags(@RequestParam Set<String> tags) {
        return postService.findPostsByTags(tags);
    }

    @GetMapping("/post/period")
    public List<PostDto> findPostsByPeriod(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                           @RequestParam("to")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return postService.findPostsByPeriod(from, to);
    }

    @PutMapping("/post/{id}")
    public PostDto updatePost(@PathVariable String id, @RequestBody NewPostDto newPostDto) {
        return postService.updatePost(id, newPostDto);
    }
}