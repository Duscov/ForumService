package cohort_65.java.forumservice.accounting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class UserRegisterDto {
    @NotBlank(message = "Login must not be blank")
    @Email(message = "Login must be a valid email")
    String login;

    @Length(min = 8, message = "Password must be at least 8 characters long")
    @NotBlank(message = "Password must not be blank")
    @Pattern(regexp = ".*!+" , message = "Password must contain '!' character")
    String password;
    String firstName;
    String lastName;
}