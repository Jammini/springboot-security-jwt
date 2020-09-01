#### Session Based Auth

![sessionbased](https://user-images.githubusercontent.com/59176149/91828213-cb591680-ec7a-11ea-800c-cec03dff8a54.png)

server 는 내부에서 Session ID를 관리해줌.

내부 DB에서 Session ID가 존재한다면 로그인 됬을때만 보여질화면들을 보여준다.

서버가 여러개일 경우 각각의 서버가 다른 서비스를 제공해주는 케이스가 생김.

이러한 여러개의 서비스와 서버는 하나의 세션 ID를 가지고 관리를 해주기가 매우 복잡.



#### Token Based Auth (JWT)

![tokenbased](https://user-images.githubusercontent.com/59176149/91828237-d4e27e80-ec7a-11ea-84eb-d610820e620b.png)

server에서는 이 토큰이 맞다고 확인이 되면 그에 맞는 Authorization 권한을 클라이언트에게 줌.

세션과는 달리 서버 내부적으로 session ID라는 것을 관리 X



#### Session Based Auth  vs  Token Based Auth (JWT)

| Session Based Auth                                           | Token Based Auth (JWT)                                     |
| :----------------------------------------------------------- | ---------------------------------------------------------- |
| 서버 측에서 사용자들의 정보 기억                             | 헤더에 토큰을 함께 보내 유효성 검사                        |
| 로그인 중인 사용자가 많아질 경우 서버의 RAM부하              | 토큰이 클라측에 저장되기 때문에 서버는 완전히 Stateless함. |
| 세션을 사용한다면 세션을 분산시키는 시스템 설계를 해야함 -> 매우 복잡 | 클라와 서버의 연결고리가 없기 때문에 확장성이 좋다.        |
| CORS -> 세션을 관리할때 자주 사용되는 쿠키는 단일 도메인 및 서브 도메인에서만 작동 따라서 여러 도메인 관리는 번거로움. | 쿠키를 사용하지 않음(CORS문제점 해결)                      |



#### JWT

Json 포맷을 이용하여 사용자에 대한 속성을 저장하는 Claim기반의 Web Token

토큰 자체를 정보로 사용하는 방식으로 정보를 안전하게 전달

Header, Payload, Signature로 구분자 ' . ' 을 이용하여 3부분으로 나누어짐.

https://jwt.io/

1. Header

   typ와 alg 두 가지 정보 구성

   alg는 Signature를 해싱하기 위한 알고리즘 지정

2. Payload

   토큰에서 사용할 정보의 조각들인 클레임(Claim)이 담겨있음.

   2.1 등록된 클레임

   ​	토큰 정보를 표현하기 위해 이미 정해진 종류의 데이터

   - iss: 토큰 발급자(issuer)
   - sub: 토큰 제목(subject)
   - aud: 토큰 대상자(audience)
   - exp: 토큰 만료 시간(expiration), NumericDate 형식으로 되어 있어야 함 ex) 1480849147370
   - nbf: 토큰 활성 날짜(not before), 이 날이 지나기 전의 토큰은 활성화되지 않음
   - iat: 토큰 발급 시간(issued at), 토큰 발급 이후의 경과 시간을 알 수 있음
   - jti: JWT 토큰 식별자(JWT ID), 중복 방지를 위해 사용하며, 일회용 토큰(Access Token) 등에 사용

   2.2 공개 클레임

   ​	사용자 정의 클레임으로, 공개용 정보를 위해 사용된다. 충돌 방지를 위해 URI 포맷을 이용

   2.3 비공개 클레임

   ​	사용자 정의 클레임으로, 서버와 클라이언트 사이에 임의로 지정한 정보를 저장

   

3. Signature

   토큰을 인코딩하거나 유효성 검증을 할 때 사용하는 고유한 암호화 코드.

   헤더(Header)와 페이로드(Payload)의 값을 각각 BASE64로 인코딩하고, 인코딩한 값을 비밀 키를 이용해 헤더(Header)에서 정의한 알고리즘으로 해싱을 하고, 이 값을 다시 BASE64로 인코딩하여 생성



#### [jwt 단점 및 고려사항]

- Self-contained: 토큰 자체에 정보를 담고 있으므로 양날의 검이 될 수 있다.
- 토큰 길이: 토큰의 페이로드(Payload)에 3종류의 클레임을 저장하기 때문에, 정보가 많아질수록 토큰의 길이가 늘어나 네트워크에 부하를 줄 수 있다.
- Payload 인코딩: 페이로드(Payload) 자체는 암호화 된 것이 아니라, BASE64로 인코딩 된 것이다. 중간에 Payload를 탈취하여 디코딩하면 데이터를 볼 수 있으므로, JWE로 암호화하거나 Payload에 중요 데이터를 넣지 않아야 한다.
- Stateless: JWT는 상태를 저장하지 않기 때문에 한번 만들어지면 제어가 불가능하다. 즉, 토큰을 임의로 삭제하는 것이 불가능하므로 토큰 만료 시간을 꼭 넣어주어야 한다.
- Tore Token: 토큰은 클라이언트 측에서 관리해야 하기 때문에, 토큰을 저장해야 한다.



#### Test

**-  사용자 가입 **

![join](https://user-images.githubusercontent.com/59176149/91828109-afee0b80-ec7a-11ea-9eeb-f0f92be88e27.png)

/join 으로 POST request를 하여 회원가입을 진행합니다.



**- 로그인**

![login](https://user-images.githubusercontent.com/59176149/91828697-6c47d180-ec7b-11ea-93c5-2b4a516ed49f.png)

/login url에 email과 password를 입력하고 POST로 요청(Request)을 보내면 토큰이 생성하여 반환하는 것을 볼 수 있다.



**authorization request**



![member200](https://user-images.githubusercontent.com/59176149/91829159-0ad43280-ec7c-11ea-9082-7dde64857a20.png)

이후 획득한 JWT Token을 header에 담아 authorization request를 요청하면 정상적으로 결과 값을 return 한다.



![member403](https://user-images.githubusercontent.com/59176149/91829430-6e5e6000-ec7c-11ea-94e1-c214a80fcd52.png)

JWT Token을 header에 담지 않은 경우 위와 같은 오류가 발생하게 된다.