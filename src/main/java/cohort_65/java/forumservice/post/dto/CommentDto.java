package cohort_65.java.forumservice.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    String user;
    String message;
    Integer likes;
    LocalDateTime dateCreated;
}