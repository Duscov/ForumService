package cohort_65.java.forumservice.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    String login;
    String password;
    String firstName;
    String lastName;
}