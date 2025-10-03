package cohort_65.java.forumservice.post.service;

import cohort_65.java.forumservice.post.dto.DatePeriodDto;
import cohort_65.java.forumservice.post.dto.NewCommentDto;
import cohort_65.java.forumservice.post.dto.NewPostDto;
import cohort_65.java.forumservice.post.dto.PostDto;

import java.util.Set;

public interface PostService {

    PostDto addNewPost(NewPostDto newPostDto, String author);

    PostDto getPostById(String id);

    void likePost(String id);

    PostDto deletePostById(String id);

    PostDto updatePostById(NewPostDto newPostDto, String id);

    PostDto addComment(String id, String user, NewCommentDto newCommentDto);

    Iterable<PostDto> getPostsByAuthor(String author);

    Iterable<PostDto> getPostsByTags(Set<String> tags);

    Iterable<PostDto> getPostsByPeriod(DatePeriodDto datePeriodDto);

    Iterable<PostDto> getPostByTitle(String title);
}