package cohort_65.java.forumservice.post.service;

import cohort_65.java.forumservice.post.dao.PostRepository;
import cohort_65.java.forumservice.post.dto.DatePeriodDto;
import cohort_65.java.forumservice.post.dto.NewCommentDto;
import cohort_65.java.forumservice.post.dto.NewPostDto;
import cohort_65.java.forumservice.post.dto.PostDto;
import cohort_65.java.forumservice.post.dto.exception.PostNotFoundException;
import cohort_65.java.forumservice.post.model.Comment;
import cohort_65.java.forumservice.post.model.Post;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    ModelMapper modelMapper;

    @MockitoBean
    PostRepository postRepository;

    @Test
    void addNewPost() {
        NewPostDto newPostDto = new NewPostDto("title1", "content1", Set.of("tag1", "tag2"));
        Post post = new Post(newPostDto.getTitle(), newPostDto.getContent(), "author1", newPostDto.getTags());
        post.setId("1");

        Mockito.when(postRepository.save(any(Post.class))).thenReturn(post);

        PostDto result = postService.addNewPost(newPostDto, "author1");

        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getAuthor()).isEqualTo("author1");
        assertThat(result.getTitle()).isEqualTo("title1");
        assertThat(result.getTags()).containsExactlyInAnyOrder("tag1", "tag2");

        Mockito.verify(postRepository).save(any(Post.class));
    }

    @Test
    void getPostById_found() {
        Post post = new Post("t", "c", "author", Set.of("tag"));
        post.setId("42");
        Mockito.when(postRepository.findById("42")).thenReturn(Optional.of(post));

        PostDto dto = postService.getPostById("42");

        assertThat(dto.getId()).isEqualTo("42");
        assertThat(dto.getAuthor()).isEqualTo("author");
        Mockito.verify(postRepository).findById("42");
    }

    @Test
    void getPostById_notFound() {
        Mockito.when(postRepository.findById("99")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getPostById("99"))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void likePost_incrementsLikes() {
        Post post = new Post("t", "c", "a", Set.of());
        post.setId("1");
        Mockito.when(postRepository.findById("1")).thenReturn(Optional.of(post));
        Mockito.when(postRepository.save(any(Post.class))).thenReturn(post);

        postService.likePost("1");

        assertThat(post.getLikes()).isEqualTo(1);
        Mockito.verify(postRepository).save(post);
    }

    @Test
    void likePost_notFound() {
        Mockito.when(postRepository.findById("404")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.likePost("404"))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void deletePostById_success() {
        Post post = new Post("t", "c", "a", Set.of("tag"));
        post.setId("del1");
        Mockito.when(postRepository.findById("del1")).thenReturn(Optional.of(post));

        PostDto dto = postService.deletePostById("del1");

        assertThat(dto.getId()).isEqualTo("del1");
        Mockito.verify(postRepository).delete(post);
    }

    @Test
    void deletePostById_notFound() {
        Mockito.when(postRepository.findById("del404")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.deletePostById("del404"))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void updatePostById_changesFields() {
        Set<String> tags = new HashSet<>();
        tags.add("x");
        Post existing = new Post("old title", "old content", "author", tags);
        existing.setId("up1");

        NewPostDto update = new NewPostDto("new title", "new content", Set.of("y"));

        Mockito.when(postRepository.findById("up1")).thenReturn(Optional.of(existing));
        Mockito.when(postRepository.save(any(Post.class))).thenReturn(existing);

        PostDto dto = postService.updatePostById(update, "up1");

        assertThat(dto.getTitle()).isEqualTo("new title");
        assertThat(dto.getContent()).isEqualTo("new content");
        assertThat(dto.getTags()).contains("x", "y"); // старые + новые
        Mockito.verify(postRepository).save(existing);
    }

    @Test
    void updatePostById_notFound() {
        NewPostDto update = new NewPostDto("new title", "new content", Set.of("y"));
        Mockito.when(postRepository.findById("up404")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.updatePostById(update, "up404"))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void addComment_success() {
        Post post = new Post("t", "c", "a", Set.of());
        post.setId("c1");
        Mockito.when(postRepository.findById("c1")).thenReturn(Optional.of(post));
        Mockito.when(postRepository.save(any(Post.class))).thenReturn(post);

        NewCommentDto newCommentDto = new NewCommentDto("hello!");
        PostDto dto = postService.addComment("c1", "user1", newCommentDto);

        assertThat(dto.getComments()).hasSize(1);
        Comment added = post.getComments().get(0);
        assertThat(added.getUser()).isEqualTo("user1");
        assertThat(added.getMessage()).isEqualTo("hello!");
        Mockito.verify(postRepository).save(post);
    }

    @Test
    void addComment_notFound() {
        Mockito.when(postRepository.findById("c404")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.addComment("c404", "user1", new NewCommentDto("msg")))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void getPostsByAuthor_returnsList() {
        Post p1 = new Post("t1", "c1", "authorA", Set.of("x"));
        p1.setId("1");
        Post p2 = new Post("t2", "c2", "authorA", Set.of("y"));
        p2.setId("2");

        Mockito.when(postRepository.findAllByAuthorIgnoreCase("authorA"))
                .thenReturn(List.of(p1, p2));

        Iterable<PostDto> result = postService.getPostsByAuthor("authorA");

        assertThat(result).hasSize(2);
    }

    @Test
    void getPostsByTags_returnsFiltered() {
        Post p1 = new Post("t1", "c1", "a", Set.of("java"));
        p1.setId("1");
        Mockito.when(postRepository.findAllByTagsIgnoreCaseIn(Set.of("java")))
                .thenReturn(List.of(p1));

        Iterable<PostDto> result = postService.getPostsByTags(Set.of("java"));

        assertThat(result).hasSize(1);
        assertThat(result.iterator().next().getTags()).contains("java");
    }

    @Test
    void getPostsByPeriod_returnsFiltered() {
        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now().plusDays(1);

        Post p = new Post("t", "c", "a", Set.of());
        p.setId("p1");
        p.setDateCreated(LocalDateTime.now());

        DatePeriodDto dto = new DatePeriodDto(from.toLocalDate(), to.toLocalDate());
        Mockito.when(postRepository.findAllByDateCreatedBetween(from.toLocalDate(), to.toLocalDate()))
                .thenReturn(List.of(p));

        Iterable<PostDto> result = postService.getPostsByPeriod(dto);

        assertThat(result).hasSize(1);
        assertThat(result.iterator().next().getId()).isEqualTo("p1");
    }

    @Test
    void getPostByTitle() {
        Post p1 = new Post("t1", "c1", "authorA", Set.of("x"));
        p1.setId("1");
        Post p2 = new Post("t2", "c2", "authorA", Set.of("y"));
        p2.setId("2");
        Post p3 = new Post("t2", "c3", "authorB", Set.of("w"));
        p2.setId("3");

        Mockito.when(postRepository.findAllByTitleIgnoreCase("t2"))
                .thenReturn(List.of(p2, p3));

        Iterable<PostDto> result = postService.getPostByTitle("t2");

        assertThat(result).hasSize(2);
        assertThat(result.iterator().next().getTitle()).isEqualTo("t2");
        assertThat(result.iterator().next().getAuthor()).isEqualTo("authorA");
        assertThat(result.iterator().next().getTags()).contains("x", "y", "w");
    }
}
