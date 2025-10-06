package cohort_65.java.forumservice.accounting.service;

import cohort_65.java.forumservice.accounting.dao.UserAccountRepository;
import cohort_65.java.forumservice.accounting.dto.UserDto;
import cohort_65.java.forumservice.accounting.dto.UserRegisterDto;
import cohort_65.java.forumservice.accounting.dto.UserUpdateDto;
import cohort_65.java.forumservice.accounting.dto.exception.UserExistsException;
import cohort_65.java.forumservice.accounting.dto.exception.UserNotFoundException;
import cohort_65.java.forumservice.accounting.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    final UserAccountRepository userAccountRepository;
    final ModelMapper modelMapper;
    final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        if (userAccountRepository.existsById(userRegisterDto.getLogin())) {
            throw new UserExistsException();
        }
        UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);

        String password = passwordEncoder.encode(userAccount.getPassword());
        userAccount.setPassword(password);

        userAccount = userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto getUserByLogin(String login) {
        UserAccount userAccount = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto removeUserByLogin(String login) {
        UserAccount userAccount = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);
        userAccountRepository.delete(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto updateUserByLogin(String login, UserUpdateDto userUpdateDto) {
        UserAccount userAccount = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);
        if (userUpdateDto.getLastName() != null) {
            userAccount.setLastName(userUpdateDto.getLastName());
        }
        if (userUpdateDto.getFirstName() != null) {
            userAccount.setFirstName(userUpdateDto.getFirstName());
        }
        userAccount = userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto changeRoleForUser(String login, String role, boolean isAddRole) {
        UserAccount userAccount = userAccountRepository.findById(login)
                .orElseThrow(UserNotFoundException::new);
        boolean res = isAddRole ? userAccount.addRole(role) : userAccount.removeRole(role);
        if (res) userAccount = userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }
}