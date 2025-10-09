package cohort_65.java.forumservice.accounting.dto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserExistsException extends RuntimeException {
    public UserExistsException(String login) {
        super("User with login " + login + "already exists");
    }
}