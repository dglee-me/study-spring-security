package kr.co.dglee.study.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  /**
   * 인증 성공 후에만 접근 가능 테스트 API
   *
   * @return
   */
  @GetMapping("/hello")
  public String hello() {
    return "Hello, World!";
  }
}
