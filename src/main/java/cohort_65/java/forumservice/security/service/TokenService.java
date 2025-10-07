package cohort_65.java.forumservice.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {

    private final SecretKey accessKey;
    private final SecretKey refreshKey;

    public TokenService(@Value("${key.access}") String accessPhrase,
                        @Value("${key.refresh}") String refreshPhrase) {
        accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessPhrase));
        refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshPhrase));
    }

    public String generateAccessToken(String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 1000 * 60 * 60);


        return Jwts.builder()
                .subject(username)
                .expiration(expirationDate)
                .signWith(accessKey)
                .compact();
    }

    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 1000 * 60 * 60 * 24);

        return Jwts.builder()
                .subject(username)
                .expiration(expirationDate)
                .signWith(refreshKey)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, accessKey);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, refreshKey);
    }

    private boolean validateToken(String token, SecretKey key) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims getAccessTokenClaims(String accessToken) {
        return getClaims(accessToken,accessKey);
    }

    public Claims getRefreshTokenClaims(String refreshToken) {
        return getClaims(refreshToken,refreshKey);
    }

    private Claims getClaims(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public String getTokenFromRequest(HttpServletRequest request, String tokenParamName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(tokenParamName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
