package kr.co.dglee.study.provider;

import kr.co.dglee.study.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class AuthProvider implements AuthenticationProvider {

  private final MemberService memberService;

  private final PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    final UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;

    final String email = authToken.getName();
    final String password = (String) authToken.getCredentials();

    UserDetails member = memberService.loadUserByUsername(email);
    if (!passwordEncoder.matches(password, member.getPassword())) {
      throw new BadCredentialsException(member.getUsername() + " Invalid password");
    }

    return new UsernamePasswordAuthenticationToken(member, password, member.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
