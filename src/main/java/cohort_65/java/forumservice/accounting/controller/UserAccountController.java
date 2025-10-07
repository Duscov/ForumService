package cohort_65.java.forumservice.accounting.controller;

import cohort_65.java.forumservice.accounting.dto.UserDto;
import cohort_65.java.forumservice.accounting.dto.UserRegisterDto;
import cohort_65.java.forumservice.accounting.dto.UserUpdateDto;
import cohort_65.java.forumservice.accounting.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class UserAccountController {

    final UserAccountService userAccountService;

    @PostMapping("/register")
    public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
        return userAccountService.register(userRegisterDto);
    }

    @GetMapping("/user/{login}")
    public UserDto getUserByLogin(@PathVariable String login) {
        return userAccountService.getUserByLogin(login);
    }

    @DeleteMapping("/user/{login}")
    public UserDto removeUserByLogin(@PathVariable String login) {
        return userAccountService.removeUserByLogin(login);
    }

    @PutMapping("/user/{login}")
    public UserDto updateUserByLogin(@PathVariable String login,
                                     @RequestBody UserUpdateDto userUpdateDto) {
        return userAccountService.updateUserByLogin(login, userUpdateDto);
    }

    @PutMapping("/user/{login}/role/{role}")
    public UserDto addRoleForUser(@PathVariable String login,
                                  @PathVariable String role) {
        return userAccountService.changeRoleForUser(login, role, true);
    }

    @DeleteMapping("/user/{login}/role/{role}")
    public UserDto removeRoleForUser(@PathVariable String login,
                                     @PathVariable String role) {
        return userAccountService.changeRoleForUser(login, role, false);
    }

    @PostMapping("/login")
    public UserDto login(Principal principal) {
        return userAccountService.getUserByLogin(principal.getName());
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(Principal principal, @RequestHeader("X-Password") String newPassword) {
        userAccountService.changePassword(principal.getName(), newPassword);
    }
}