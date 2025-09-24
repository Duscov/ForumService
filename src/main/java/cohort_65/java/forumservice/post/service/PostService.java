package cohort_65.java.forumservice.post.service;

import cohort_65.java.forumservice.post.dto.NewPostDto;
import cohort_65.java.forumservice.post.dto.PostDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface PostService {

    PostDto addNewPost(NewPostDto newPostDto, String author);

    PostDto findPostById(String id);

    void addLike(String id);

    List<PostDto> findPostsByAuthor(String author);

    void addComment(String postId, String user, String message);

    void deletePost(String id);

    List<PostDto> findPostsByTags(Set<String> tags);

    List<PostDto> findPostsByPeriod(LocalDateTime from, LocalDateTime to);

    PostDto updatePost(String id, NewPostDto newPostDto);
}