package cohort_65.java.forumservice.post.dto;

import cohort_65.java.forumservice.post.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    String id;
    String title;
    String content;
    String author;
    @Singular
    Set<String> tags;
    int likes;
    LocalDateTime dateCreated;
    @Singular
    List<CommentDto> comments;
}
