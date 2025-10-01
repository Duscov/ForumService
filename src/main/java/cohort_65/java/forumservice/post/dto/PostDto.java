package cohort_65.java.forumservice.post.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    private String id;
    private String title;
    private String content;
    private String author;
    @Singular
    private List<String> tags;
    private int likes;
    private LocalDateTime dateCreated;
    @Singular
    private List<CommentDto> comments;
}