package kr.co.dglee.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StudySpringSecurityApplication {

  public static void main(String[] args) {
    SpringApplication.run(StudySpringSecurityApplication.class, args);
  }

}
