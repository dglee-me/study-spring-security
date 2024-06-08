package kr.co.dglee.study.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kr.co.dglee.study.entity.Member;
import kr.co.dglee.study.utils.JwtUtil;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AuthFilter extends UsernamePasswordAuthenticationFilter {

  private final JwtUtil jwtUtil;

  public AuthFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
    super(authenticationManager);
    this.jwtUtil = jwtUtil;
  }

  /**
   * 인증 성공 시
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authentication) {

    // 인증 성공 시 JWT 생성하여 응답 바디에 담아서 전달 (LoginResponseDTO)
    Member member = (Member) authentication.getPrincipal();

    final String accessToken = jwtUtil.createToken("access", member.getEmail(), member.getRole().name());
    final String refreshToken = jwtUtil.createToken("refresh", member.getEmail(), member.getRole().name());

    // 응답 바디에 담아서 전달
    try {
      // json 형태로 변환하여 응답
      response.setStatus(HttpStatus.OK.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter().write(new ObjectMapper().writeValueAsString(new LoginResponseDTO(accessToken, refreshToken)));
    } catch (IOException e) {
      logger.error("", e);
    }
  }

  /**
   * 인증 실패 시
   */
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    // 인증 실패 시 처리
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
  }

  /**
   * 로그인 요청 정보를 받아온다.
   */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    // 로그인 요청한 정보를 받아온다.
    try {
      LoginRequestDTO loginRequestDTO = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDTO.class);

      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequestDTO.email, loginRequestDTO.password);
      return getAuthenticationManager().authenticate(authToken);
    } catch (IOException e) {
      throw new BadCredentialsException("로그인 정보를 읽어오는데 실패했습니다.", e);
    }
  }

  private record LoginRequestDTO(String email, String password) {

  }

  @Getter
  private class LoginResponseDTO {

    private String accessToken;
    private String refreshToken;

    public LoginResponseDTO(String accessToken, String refreshToken) {
      this.accessToken = accessToken;
      this.refreshToken = refreshToken;
    }
  }
}
