package cohort_65.java.forumservice.accounting.service;

import cohort_65.java.forumservice.accounting.dao.UserAccountRepository;
import cohort_65.java.forumservice.accounting.dto.*;
import cohort_65.java.forumservice.accounting.dto.exception.*;
import cohort_65.java.forumservice.accounting.model.Role;
import cohort_65.java.forumservice.accounting.model.UserAccount;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.util.Optional;
import java.util.Set;
import static cohort_65.java.forumservice.accounting.model.Role.MODER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserAccountServiceTest {

    @Autowired
    UserAccountService userAccountService;

    @MockitoBean
    UserAccountRepository userAccountRepository;

    @Autowired
    ModelMapper modelMapper;

    @Test
    void register_test() {
        UserRegisterDto dto1 = new UserRegisterDto("user1", "pass1", "Alice", "Smith");
        UserRegisterDto dto2 = new UserRegisterDto("user2", "pass2", "Bob", "Brown");
        UserRegisterDto dto3 = new UserRegisterDto("user3", "pass3", "Cara", "Moon");

        Mockito.when(userAccountRepository.existsById(any())).thenReturn(false);
        Mockito.when(userAccountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserDto result1 = userAccountService.register(dto1);
        UserDto result2 = userAccountService.register(dto2);
        UserDto result3 = userAccountService.register(dto3);

        assertThat(result1.getLogin()).isEqualTo("user1");
        assertThat(result1.getFirstName()).isEqualTo("Alice");
        assertThat(result1.getLastName()).isEqualTo("Smith");

        assertThat(result2.getLogin()).isEqualTo("user2");
        assertThat(result2.getFirstName()).isEqualTo("Bob");

        assertThat(result3.getLogin()).isEqualTo("user3");
        assertThat(result3.getLastName()).isEqualTo("Moon");
    }

    @Test
    void getUserByLogin_test() {
        UserAccount u1 = new UserAccount("login1", "pass1", "Alice", "Smith");
        UserAccount u2 = new UserAccount("login2", "pass2", "Bob", "Brown");
        UserAccount u3 = new UserAccount("login3", "pass3", "Cara", "Moon");
        u2.getRoles().add(Role.ADMIN);
        u3.getRoles().add(Role.MODER);

        Mockito.when(userAccountRepository.findById("login1")).thenReturn(Optional.of(u1));
        Mockito.when(userAccountRepository.findById("login2")).thenReturn(Optional.of(u2));
        Mockito.when(userAccountRepository.findById("login3")).thenReturn(Optional.of(u3));

        UserDto result1 = userAccountService.getUserByLogin("login1");
        UserDto result2 = userAccountService.getUserByLogin("login2");
        UserDto result3 = userAccountService.getUserByLogin("login3");

        assertThat(result1.getLogin()).isEqualTo("login1");
        assertThat(result1.getFirstName()).isEqualTo("Alice");
        assertThat(result1.getRoles()).isEqualTo(Role.USER);

        assertThat(result2.getLogin()).isEqualTo("login2");
        assertThat(result2.getRoles()).isIn(Role.USER, Role.ADMIN);

        assertThat(result3.getLogin()).isEqualTo("login3");
        assertThat(result3.getRoles()).isIn(Role.USER, Role.MODER);
    }

    @Test
    void removeUserByLogin_test() {
        UserAccount u1 = new UserAccount("login1", "pass1", "Alice", "Smith");
        UserAccount u2 = new UserAccount("login2", "pass2", "Bob", "Brown");
        UserAccount u3 = new UserAccount("login3", "pass3", "Cara", "Moon");
        u2.getRoles().add(Role.ADMIN);
        u3.getRoles().add(Role.MODER);

        Mockito.when(userAccountRepository.findById("login1")).thenReturn(Optional.of(u1));
        Mockito.when(userAccountRepository.findById("login2")).thenReturn(Optional.of(u2));
        Mockito.when(userAccountRepository.findById("login3")).thenReturn(Optional.of(u3));

        UserDto result1 = userAccountService.removeUserByLogin("login1");
        UserDto result2 = userAccountService.removeUserByLogin("login2");
        UserDto result3 = userAccountService.removeUserByLogin("login3");

        assertThat(result1.getLogin()).isEqualTo("login1");
        assertThat(result1.getRoles()).isEqualTo("USER");

        assertThat(result2.getLogin()).isEqualTo("login2");
        assertThat(result2.getRoles()).isIn("USER", "ADMIN");

        assertThat(result3.getLogin()).isEqualTo("login3");
        assertThat(result3.getRoles()).isIn("USER", "MODER");

        Mockito.verify(userAccountRepository).delete(u1);
        Mockito.verify(userAccountRepository).delete(u2);
        Mockito.verify(userAccountRepository).delete(u3);
    }

    @Test
    void updateUserByLogin_test() {
        UserAccount u1 = new UserAccount("login1", "pass1", "Alice", "Smith");
        UserAccount u2 = new UserAccount("login2", "pass2", "Bob", "Brown");
        UserAccount u3 = new UserAccount("login3", "pass3", "Cara", "Moon");

        Mockito.when(userAccountRepository.findById("login1")).thenReturn(Optional.of(u1));
        Mockito.when(userAccountRepository.findById("login2")).thenReturn(Optional.of(u2));
        Mockito.when(userAccountRepository.findById("login3")).thenReturn(Optional.of(u3));
        Mockito.when(userAccountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserUpdateDto update1 = new UserUpdateDto("Alicia", null); // меняем firstName
        UserUpdateDto update2 = new UserUpdateDto(null, "White");  // меняем lastName
        UserUpdateDto update3 = new UserUpdateDto("Caroline", "Moonlight"); // оба поля

        UserDto result1 = userAccountService.updateUserByLogin("login1", update1);
        UserDto result2 = userAccountService.updateUserByLogin("login2", update2);
        UserDto result3 = userAccountService.updateUserByLogin("login3", update3);

        assertThat(result1.getFirstName()).isEqualTo("Alicia");
        assertThat(result1.getLastName()).isEqualTo("Smith");

        assertThat(result2.getFirstName()).isEqualTo("Bob");
        assertThat(result2.getLastName()).isEqualTo("White");

        assertThat(result3.getFirstName()).isEqualTo("Caroline");
        assertThat(result3.getLastName()).isEqualTo("Moonlight");
    }

    @Test
    void changeRoleForUser_test() {
        UserAccount u1 = new UserAccount("login1", "pass1", "Alice", "Smith");
        UserAccount u2 = new UserAccount("login2", "pass2", "Bob", "Brown");
        UserAccount u3 = new UserAccount("login3", "pass3", "Cara", "Moon");
        u2.getRoles().add(Role.ADMIN);
        u3.getRoles().add(Role.MODER);

        Mockito.when(userAccountRepository.findById("login1")).thenReturn(Optional.of(u1));
        Mockito.when(userAccountRepository.findById("login2")).thenReturn(Optional.of(u2));
        Mockito.when(userAccountRepository.findById("login3")).thenReturn(Optional.of(u3));
        Mockito.when(userAccountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserDto result1 = userAccountService.changeRoleForUser("login1", "MODER", true); // добавляем MODER
        UserDto result2 = userAccountService.changeRoleForUser("login2", "ADMIN", false); // удаляем ADMIN
        UserDto result3 = userAccountService.changeRoleForUser("login3", "USER", true); // добавляем USER (уже есть)

        assertThat(result1.getRoles()).isIn(Set.of("USER", "MODER"), Set.of("MODER", "USER"));
        assertThat(result2.getRoles()).isEqualTo(Set.of("USER"));
        assertThat(result3.getRoles()).isEqualTo(Set.of("USER", "MODER")); // если USER уже был, роли не меняются
    }


}
