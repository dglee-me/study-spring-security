## 스프링 시큐리티 학습

### 아이디/비밀번호 인증 (최초 로그인)

최초 로그인 시 발급받은 토큰이 없기 때문에 아이디/비밀번호로 인증을 받습니다.

```text
1. AuthFilter (UsernamePasswordAuthenticationFilter를 상속받아 구현된 필터)에서 인증을 시도합니다.
2. AuthProvider.authenticate()에서 실제 인증 조회를 합니다.
3. 인증이 성공하면 accessToken과 refreshToken과 함께 200 OK를 반환합니다.
4. 인증이 실패하면 401 Unauthorized를 반환합니다.
```

### JWT 인증 (최초 로그인 이후 access token으로 인증)

권한이 필요한 API를 호출 시 최초 로그인 이후 발급받은 access token으로 인증을 받습니다.

```text
1. JwtFilter에서 토큰을 검증합니다.
2. 검증이 성공하면 SecurityContextHolder에 인증 정보를 저장합니다.
3. 인증이 성공하면 200 OK를 반환합니다.
4. 인증이 실패하면 401 Unauthorized를 반환합니다.
```

### 로그인 API
/auth/token으로 POST 요청을 보내면 토큰을 발급받을 수 있습니다.

이 URI는 SecurityConfig 클래스에서 filterProcessesUrl()로 설정되어 있습니다. 

(별도의 설정이 없으면 /login으로 설정됩니다.)

```http request
POST http://localhost:8080/auth/token
Content-Type: application/json

{
  "email": "dglee.dev@gmail.com",
  "password": "1234"
}
```
