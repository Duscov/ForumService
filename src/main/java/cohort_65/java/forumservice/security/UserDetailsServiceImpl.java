package cohort_65.java.forumservice.security;

import cohort_65.java.forumservice.accounting.dao.UserAccountRepository;
import cohort_65.java.forumservice.accounting.dto.exception.UserNotFoundException;
import cohort_65.java.forumservice.accounting.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findById(username).orElseThrow(UserNotFoundException::new);

        Collection<String> authorities = userAccount.getRoles()
                .stream()
                .map(r -> "ROLE_" + r.name())
                .toList();

        return new User(username, userAccount.getPassword(), AuthorityUtils.createAuthorityList(authorities));
    }
}