package kr.co.dglee.study;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class StudySpringSecurityApplicationTests {

  @Test
  void encryptPassword() {

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String password = "1234";

    // String encodedPassword = passwordEncoder.encode(password); $2a$10$zN7z1o7C/lPpOWQMrDUAJOSz8X166SfR9GAjw1P600Zi/WUJVzili
    Assertions.assertTrue(passwordEncoder.matches(password, "$2a$10$zN7z1o7C/lPpOWQMrDUAJOSz8X166SfR9GAjw1P600Zi/WUJVzili"));
  }
}
