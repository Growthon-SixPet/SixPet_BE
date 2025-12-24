package growthon.withtail_be.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String token = tokenProvider.resolveToken(request);

        try {
            // 토큰이 있으면 검증 + 인증 세팅
            if (StringUtils.hasText(token)) {
                tokenProvider.validateToken(token);

                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            writeError(request, response, "JWT_402", "만료된 토큰입니다.");

        } catch (SecurityException | MalformedJwtException e) {
            writeError(request, response, "JWT_401", "유효하지 않은 토큰입니다.");

        } catch (IllegalArgumentException e) {
            writeError(request, response, "JWT_404", "토큰이 비어있거나 잘못되었습니다.");

        } catch (Exception e) {
            writeError(request, response, "JWT_500", "JWT 처리 중 알 수 없는 오류가 발생했습니다.");
        }
    }

    private void writeError(HttpServletRequest req, HttpServletResponse res, String code, String message) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json;charset=UTF-8");

        String origin = req.getHeader("Origin");
        if (origin != null && !origin.isBlank()) {
            res.setHeader("Access-Control-Allow-Origin", origin);
            res.setHeader("Vary", "Origin");
            res.setHeader("Access-Control-Allow-Credentials", "true");
            res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
            res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
        }

        Map<String, Object> body = Map.of(
                "isSuccess", false,
                "statusCode", code,
                "message", message,
                "result", null
        );

        res.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
