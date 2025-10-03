package cohort_65.java.forumservice.post.controller;

import cohort_65.java.forumservice.post.dto.*;
import cohort_65.java.forumservice.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void addNewPost() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        NewPostDto newPostDto = new NewPostDto("title1", "content1", Set.of("tag1", "tag2"));
        PostDto postDto = PostDto.builder()
                .id("1")
                .author("author1")
                .title("title1")
                .content("content1")
                .tags(Set.of("tag1", "tag2"))
                .likes(0)
                .dateCreated(now)
                .build();

        Mockito.when(postService.addNewPost(any(NewPostDto.class), eq("author1"))).thenReturn(postDto);

        mockMvc.perform(post("/forum/post/author1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPostDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.author").value("author1"))
                .andExpect(jsonPath("$.tags[0]").value("tag1"))
                .andExpect(jsonPath("$.tags[1]").value("tag2"))
                .andExpect(jsonPath("$.likes").value(0))
                .andExpect(jsonPath("$.title").value("title1"))
                .andExpect(jsonPath("$.content").value("content1"));
    }

    @Test
    void getPostById() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        PostDto postDto = PostDto.builder()
                .id("42")
                .author("userX")
                .title("Some post")
                .content("content here")
                .tags(Set.of("java", "spring"))
                .likes(5)
                .dateCreated(now)
                .build();

        Mockito.when(postService.getPostById("42")).thenReturn(postDto);

        mockMvc.perform(get("/forum/post/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("42"))
                .andExpect(jsonPath("$.author").value("userX"))
                .andExpect(jsonPath("$.tags[0]").exists())
                .andExpect(jsonPath("$.likes").value(5));
    }

    @Test
    void likePost() throws Exception {
        mockMvc.perform(put("/forum/post/123/like"))
                .andExpect(status().isNoContent());

        Mockito.verify(postService).likePost("123");
    }

    @Test
    void deletePost() throws Exception {
        PostDto deleted = PostDto.builder()
                .id("55")
                .author("author1")
                .title("deleted title")
                .content("deleted content")
                .tags(Set.of())
                .likes(0)
                .build();

        Mockito.when(postService.deletePostById("55")).thenReturn(deleted);

        mockMvc.perform(delete("/forum/post/55"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("55"))
                .andExpect(jsonPath("$.title").value("deleted title"));
    }

    @Test
    void updatePost() throws Exception {
        NewPostDto updateDto = new NewPostDto("new title", "new content", Set.of("tagX"));
        PostDto updated = PostDto.builder()
                .id("77")
                .author("author1")
                .title("new title")
                .content("new content")
                .tags(Set.of("tagX"))
                .likes(2)
                .build();

        Mockito.when(postService.updatePostById(any(NewPostDto.class), eq("77"))).thenReturn(updated);

        mockMvc.perform(put("/forum/post/77")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("77"))
                .andExpect(jsonPath("$.title").value("new title"))
                .andExpect(jsonPath("$.tags[0]").value("tagX"));
    }

    @Test
    void addComment() throws Exception {
        NewCommentDto newComment = new NewCommentDto("Nice post!");
        CommentDto commentDto = new CommentDto("user123", "Nice post!", 0, LocalDateTime.now());
        PostDto withComment = PostDto.builder()
                .id("88")
                .author("author1")
                .title("post with comment")
                .content("content...")
                .tags(Set.of("tag1"))
                .likes(0)
                .comment(commentDto) // Lombok @Singular для списка
                .build();

        Mockito.when(postService.addComment(eq("88"), eq("user123"), any(NewCommentDto.class)))
                .thenReturn(withComment);

        mockMvc.perform(put("/forum/post/88/comment/user123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("88"))
                .andExpect(jsonPath("$.comments[0].user").value("user123"))
                .andExpect(jsonPath("$.comments[0].message").value("Nice post!"));
    }

    @Test
    void getPostsByAuthor() throws Exception {
        List<PostDto> posts = List.of(
                PostDto.builder().id("1").author("authorA").title("Post1").tags(Set.of("t1")).likes(0).build(),
                PostDto.builder().id("2").author("authorA").title("Post2").tags(Set.of("t2")).likes(3).build()
        );

        Mockito.when(postService.getPostsByAuthor("authorA")).thenReturn(posts);

        mockMvc.perform(get("/forum/posts/author/authorA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author").value("authorA"))
                .andExpect(jsonPath("$[1].title").value("Post2"));
    }

    @Test
    void getPostsByTags() throws Exception {
        Set<String> tags = Set.of("java", "spring");
        List<PostDto> posts = List.of(
                PostDto.builder().id("10").author("dev1").title("Spring post").tags(Set.of("spring")).build(),
                PostDto.builder().id("11").author("dev2").title("Java post").tags(Set.of("java")).build()
        );

        Mockito.when(postService.getPostsByTags(tags)).thenReturn(posts);

        mockMvc.perform(post("/forum/posts/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tags)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tags[0]").value("spring"))
                .andExpect(jsonPath("$[1].tags[0]").value("java"));
    }

    @Test
    void getPostsByPeriod() throws Exception {
        DatePeriodDto period = new DatePeriodDto(LocalDate.parse("2023-01-01"), LocalDate.parse("2023-12-31"));
        List<PostDto> posts = List.of(
                PostDto.builder().id("21").author("A").title("2023 post").tags(Set.of("x")).build()
        );

        Mockito.when(postService.getPostsByPeriod(any(DatePeriodDto.class))).thenReturn(posts);

        mockMvc.perform(post("/forum/posts/period")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(period)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("2023 post"));
    }
}