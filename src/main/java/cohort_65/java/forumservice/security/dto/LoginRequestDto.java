package cohort_65.java.forumservice.security.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequestDto {
    String username;
    String password;
}
