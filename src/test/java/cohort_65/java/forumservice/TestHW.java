package cohort_65.java.forumservice;

import cohort_65.java.forumservice.configuration.PostServiceMockConfig;
import cohort_65.java.forumservice.post.controller.PostController;
import cohort_65.java.forumservice.post.dto.*;
import cohort_65.java.forumservice.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(PostController.class)   //тестируешь только слой контроллера, изолируя его от других компонентов
@Import(PostServiceMockConfig.class)
class TestHW {

    @Autowired
    private MockMvc mockMvc;  //имитирует HTTP-запросы к контроллеру.

    @Autowired
    private PostService postService;  //подменяет настоящий сервис мок-объектом

    @Autowired
    private ObjectMapper objectMapper;//сериализует/десериализует JSON

    @Test
    void testAddNewPost() throws Exception {
        NewPostDto newPost = new NewPostDto("Title", "Content", Set.of("java", "spring"));
        PostDto post = PostDto.builder()
                .id("1")
                .title("Title")
                .content("Content")
                .author("Author")
                .likes(0)
                .tags(Set.of("Java", "spring"))
                .comments(List.of())
                .dateCreated(LocalDateTime.now())
                .build();

        Mockito.when(postService.addNewPost(Mockito.any(), Mockito.eq("Author")))
                .thenReturn(post);

        mockMvc.perform(post("/forum/post/Author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPost)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    void testGetPostById() throws Exception {
        PostDto post = new PostDto(
                "1", "Title", "Content", "Author",
                new ArrayList<>(List.of("java", "spring")), 1,
                LocalDateTime.now(), new ArrayList(List.of("Nice post!")));
        Mockito.when(postService.getPostById("1")).thenReturn(post);

        mockMvc.perform(get("/forum/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    void testLikePost() throws Exception {
        mockMvc.perform(put("/forum/post/1/like"))
                .andExpect(status().isNoContent());

        Mockito.verify(postService).likePost("1");
    }

    @Test
    void testDeletePost() throws Exception {
        PostDto deletedPost = new PostDto(
                "1", "Title", "Content", "Author",
                new ArrayList<>(List.of("java", "spring")), 1,
                LocalDateTime.now(), new ArrayList(List.of("Nice post!")));
        Mockito.when(postService.deletePostById("1")).thenReturn(deletedPost);

        mockMvc.perform(delete("/forum/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void testUpdatePost() throws Exception {
        NewPostDto update = new NewPostDto("Updated", "Updated Content", Set.of("updated"));

        PostDto post = PostDto.builder()
                .id("1")
                .title("Updated")
                .content("Updated Content")
                .author("Author")
                .likes(0)
                .tags(Set.of("Java", "spring"))
                .comments(List.of())
                .dateCreated(LocalDateTime.now())
                .build();

        Mockito.when(postService.updatePostById(Mockito.any(NewPostDto.class), Mockito.eq("1")))
                .thenReturn(post);

        mockMvc.perform(put("/forum/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString())) // 👈 покажет JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated")); // ✅ правильный путь
    }


    @Test
    void testAddComment() throws Exception {
        NewCommentDto comment = new NewCommentDto("Nice post!");

        CommentDto commentDto = new CommentDto(
                "user1", "Nice post!", 0, LocalDateTime.now()
        );

        PostDto postWithComment = new PostDto(
                "1", "Title", "Content", "Author",
                new ArrayList<>(List.of("java", "spring")), 1,
                LocalDateTime.now(), List.of(commentDto)
        );

        Mockito.when(postService.addComment("1", "user1", comment)).thenReturn(postWithComment);

        mockMvc.perform(put("/forum/post/1/comment/user1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0].message").value("Nice post!")); // ✅ путь к полю внутри объекта
    }


    @Test
    void testGetPostsByAuthor() throws Exception {
        PostDto post = new PostDto(
                "1", "Title", "Content", "Author",
                new ArrayList<>(List.of("java", "spring")), 1,
                LocalDateTime.now(), new ArrayList(List.of("Nice post!")));
        Mockito.when(postService.getPostsByAuthor("Author")).thenReturn(List.of(post));

        mockMvc.perform(get("/forum/posts/author/Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author").value("Author"));
    }

    @Test
    void testGetPostsByTags() throws Exception {
        Set<String> tags = Set.of("java");
        PostDto post = new PostDto(
                "1", "Title", "Content", "Author",
                new ArrayList<>(List.of("java", "spring")), 1,
                LocalDateTime.now(), new ArrayList(List.of("Nice post!")));

        Mockito.when(postService.getPostsByTags(tags)).thenReturn(List.of(post));

        mockMvc.perform(post("/forum/posts/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tags)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tags[0]").value("java"));
    }

    @Test
    void testGetPostsByPeriod() throws Exception {
        DatePeriodDto period = new DatePeriodDto(
                LocalDate.parse("2023-01-01T00:00:00"),
                LocalDate.parse("2023-12-31T23:59:00"));
        PostDto post = new PostDto(
                "1", "Title", "Content", "Author",
                new ArrayList<>(List.of("java", "spring")), 1,
                LocalDateTime.now(), new ArrayList(List.of("Nice post!")));

        Mockito.when(postService.getPostsByPeriod(period)).thenReturn(List.of(post));

        mockMvc.perform(post("/forum/posts/period")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(period)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Old Post"));
    }
}
