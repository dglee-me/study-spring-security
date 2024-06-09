package kr.co.dglee.study.filter;

import static kr.co.dglee.study.utils.JwtUtil.TOKEN_PREFIX;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import kr.co.dglee.study.enums.UserRole;
import kr.co.dglee.study.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

    // 헤더가 없거나 Prefix가 존재하지 않을 경우 중지
    if (authorization == null || !authorization.startsWith(TOKEN_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }

    final String accessToken = authorization.substring(7);

    // 만료시간 검증
    try {
      jwtUtil.isExpired(accessToken);
    } catch (ExpiredJwtException e) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().print("Token is expired");
      return;
    }

    // 카테고리가 access가 아닐 경우 중지
    String category = jwtUtil.getCategory(accessToken);
    if (!"access".equals(category)) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().print("invalid token");
      return;
    }

    final String email = jwtUtil.getUserName(accessToken);
    final UserRole role = UserRole.valueOf(jwtUtil.getRole(accessToken));

    Authentication authentication = new UsernamePasswordAuthenticationToken(
        email,
        "USE_TOKEN",
        Collections.singletonList(new SimpleGrantedAuthority(role.name())));

    // 인증 정보 저장
    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }
}
