package cohort_65.java.forumservice.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private String user;
    private String message;
    private Integer likes;
    private LocalDateTime dateCreated;
}