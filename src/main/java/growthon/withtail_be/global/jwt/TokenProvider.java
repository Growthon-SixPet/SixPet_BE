package growthon.withtail_be.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider {

    private static final String ROLE_CLAIM = "role";
    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    private final SecretKey key;
    private final long accessTokenValidityTime;
    private final long refreshTokenValidityTime;

    public TokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${ACCESS_TOKEN_VALIDITY_IN_MILLISECONDS}") long accessTokenValidityTime,
            @Value("${REFRESH_TOKEN_VALIDITY_IN_MILLISECONDS}") long refreshTokenValidityTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityTime = accessTokenValidityTime;
        this.refreshTokenValidityTime = refreshTokenValidityTime;
    }

    // AccessToken 생성
    public String createAccessToken(Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidityTime);

        return Jwts.builder()
                .subject(userId.toString())
                .claim("token_type", "access_token")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    // RefreshToken 생성
    public String createRefreshToken(Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenValidityTime);

        return Jwts.builder()
                .subject(userId.toString())
                .claim("token_type", "refresh_token")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    // Authentication 생성
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        if ("refresh_token".equals(claims.get("token_type", String.class))) {
            throw new IllegalArgumentException("리프레시 토큰으로 인증할 수 없습니다.");
        }

        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                Collections.emptyList()
        );
    }

    // 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        return true;
    }

    // Claims 파싱 (만료 토큰도 claims는 꺼내야 함)
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 만료된 토큰도 내부 정보는 필요할 수 있음
        }
    }

    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.getSubject());
    }
}
