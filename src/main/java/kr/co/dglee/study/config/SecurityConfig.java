package kr.co.dglee.study.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import kr.co.dglee.study.filter.AuthFilter;
import kr.co.dglee.study.provider.AuthProvider;
import kr.co.dglee.study.service.MemberService;
import kr.co.dglee.study.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtUtil jwtUtil;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthProvider authProvider(MemberService memberService) {
    return new AuthProvider(memberService, passwordEncoder());
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthProvider authProvider) {
    return new ProviderManager(authProvider);
  }

  @Bean
  public AuthFilter customAuthFilter(AuthenticationManager authenticationManager) {
    AuthFilter authFilter = new AuthFilter(authenticationManager, jwtUtil);
    authFilter.setFilterProcessesUrl("/auth/token");
    return authFilter;
  }

  /**
   * 정적 리소스 및 H2 콘솔 접근을 위한 설정
   *
   * @return WebSecurityCustomizer
   */
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring()
        .requestMatchers(PathRequest.toH2Console())
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
  }

  /**
   * 인증 설정
   *
   * @param http HttpSecurity
   * @return SecurityFilterChain
   * @throws Exception
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthFilter authFilter) throws Exception {

    http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)

        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/token").permitAll()
            .anyRequest().authenticated())

        .addFilterAt(authFilter, UsernamePasswordAuthenticationFilter.class); // 기본 로그인 필터 대신 사용자 정의 로그인 필터를 사용

    return http.build();
  }
}
