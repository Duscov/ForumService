package cohort_65.java.forumservice.security.service;

import cohort_65.java.forumservice.security.dto.LoginRequestDto;
import cohort_65.java.forumservice.security.dto.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    final UserDetailsServiceImpl userDetailsService;
    final PasswordEncoder passwordEncoder;
    final TokenService tokenService;
    Map<String, String> refreshStorage = new HashMap<>();

    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (passwordEncoder.matches(loginRequestDto.getPassword(), userDetails.getPassword())) {
            String accessToken = tokenService.generateAccessToken(username);
            String refreshToken = tokenService.generateRefreshToken(username);
            refreshStorage.put(refreshToken, username);
            return new TokenResponseDto(accessToken, refreshToken);
        }
        return null;
    }
}
