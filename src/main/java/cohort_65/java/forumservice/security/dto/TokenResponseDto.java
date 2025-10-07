package cohort_65.java.forumservice.security.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TokenResponseDto {
    String accessToken;
    String refreshToken;
}
