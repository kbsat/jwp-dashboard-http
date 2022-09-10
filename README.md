# 톰캣 구현하기

## 기능 요구사항

## 1. HTTP 서버 구현하기

- [x] GET /index.html 응답하기
- [x] CSS 지원하기
    - [x] 요청에 따라 다른 정적 파일 위치를 찾도록 수정
    - [x] 요청에 따라 컨텐츠 타입 찾도록 수정
- [x] Query String 파싱
    - [x] /login 페이지 처리하기
    - [x] Query String이 일치할 경우 로그를 찍어준다.

## 2. 로그인 구현하기

- [x] HTTP Reponse의 상태 응답 코드를 302로 반환한다.
    - [x] 로그인 성공 -> 302 반환 및 `/index.html` 로 리다이렉트
    - [x] 로그인 실패 -> `401.html` 로 리다이렉트
- [x] POST로 들어온 요청의 Request Body를 파싱할 수 있다.
    - [x] `/register` 로 요청시 로그인 페이지 화면을 띄워준다.
    - [x] HttpRequest가 body의 값도 가져올 수 있도록 변경한다.
    - [x] login 동작도 POST로 동작되도록 수정한다.
    - [x] RegisterController `doPost()` 구현
- [x] 로그인에 성공하면 HTTP Reponse의 헤더에 Set-Cookie 가 존재한다.
  - [x] 쿠키 객체를 구현한다.
  - [x] Set-Cookie를 구현한다.
- [x] 서버에 세션을 관리하는 클래스가 있고, 쿠키로부터 전달 받은 JSESSIONID 값이 저장된다.
  - [x] Session, SessionManager 구현한다.
  - [x] 세션 존재 시 `GET /login` 요청 시 `/index` 로 리다이렉트 되도록 수정

## 3. 리팩토링

- [x] Http11Processor에서 Parsing하는 책임 분리하기
- [x] 비즈니스 로직과 tomcat 기능을 분리하기
- [x] HttpRequest에서 일급컬렉션 사용하기
- [x] 반복되는 상수를 Enum으로 처리하기
- [x] loginController 악취 제거하기
  - [x] 컨트롤러 간 참조하는 구조 변경하기 ( LoginController, RegisterController,  -> StaticResourceController)
- [x] RequestMapping - service 과정까지에서 예외를 어떻게 처리할 것인지 고민해보기
- [x] HttpBody 와 HttpPath의 param 생성 로직이 겹친다. 이를 어떻게 처리할까?


### 예외 처리하기

- [x] param에 값이 없는 경우 처리하기 (account=&passowrd=123)
- [x] 현재 레포지터리에 없는 값으로 로그인 했을 경우 NoSuchElementException을 띄우는데 이를 처리하기
- [x] 커스텀 예외를 만들어 Http 관련 예외 처리하기

## 4. 동시성 확장하기
- [x] Executors로 Thread Pool 적용
- [x] 동시성 컬렉션 사용하기

### 테스트 보완하기

- [x] 테스트를 보완한다.
  - [x] CSS 지원 테스트
  - [x] js 지원 테스트
  - [x] 404 페이지 테스트 ( + NotFound 응답 메세지 )
  - [x] HttpRequest 잘 만들어지는지 테스트
  - [ ] 부족한 테스트 추가하기

- [x] 패키지 구조 개선하기 (사용자가 tomcat의 코드를 수정하지 않아도 서비스 코드 개선이 가능하도록!)
  - [x] `DIP`를 만족하기 위해 jwp, apache 패키지가 독자적으로 발전될 수 있도록 수정한다.
    - [x] RequestMapping 정적 클래스에서 주입받는 형식으로 변경한다. 인터페이스를 활용한다.
