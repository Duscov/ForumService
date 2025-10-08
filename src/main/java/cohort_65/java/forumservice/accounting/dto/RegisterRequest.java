package cohort_65.java.forumservice.accounting.dto;

import cohort_65.java.forumservice.aspect.Validatable;

public class RegisterRequest implements Validatable {
    private String username;
    private String password;

    @Override
    public void validate() {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
    }
}
