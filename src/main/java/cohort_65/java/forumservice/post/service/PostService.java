package cohort_65.java.forumservice.post.service;

import cohort_65.java.forumservice.post.dto.NewPostDto;
import cohort_65.java.forumservice.post.dto.PostDto;

public interface PostService {

    PostDto addNewPost(NewPostDto newPostDto, String author);
}
