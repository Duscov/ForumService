package cohort_65.java.forumservice.security.service;

import cohort_65.java.forumservice.security.dto.LoginRequestDto;
import cohort_65.java.forumservice.security.dto.TokenResponseDto;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
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
            refreshStorage.put(username, refreshToken);
            return new TokenResponseDto(accessToken, refreshToken);
        }
        return null;
    }

    public TokenResponseDto getNewTokens(HttpServletRequest request) {
        String refreshToken = tokenService.getTokenFromRequest(request, "Refresh-Token");

        if (tokenService.validateRefreshToken(refreshToken)) {
            Claims claims = tokenService.getRefreshTokenClaims(refreshToken);
            String username = claims.getSubject();
            String savedRefreshToken = refreshStorage.get(username);
            if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
                String accessToken = tokenService.generateAccessToken(username);
                String newRefreshToken = tokenService.generateRefreshToken(username);
                refreshStorage.put(username, newRefreshToken);
                return new TokenResponseDto(accessToken, newRefreshToken);
            }
        }
        return null;
    }
}
