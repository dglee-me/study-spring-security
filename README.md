## 스프링 시큐리티 학습

### 로그인

최초 이메일 / 비밀번호로 로그인 시에는 아래와 같이 동작한다.

AuthFilter (UsernamePasswordAuthenticationFilter를 상속받아 구현된 필터) -> 

AuthProvider.authenticate()에서 실제 인증 조회 ->



```http request
POST http://localhost:8080/auth/token
Content-Type: application/json

{
  "email": "dglee.dev@gmail.com",
  "password": "1234"
}
```