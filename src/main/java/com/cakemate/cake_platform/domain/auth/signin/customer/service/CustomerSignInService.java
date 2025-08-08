package com.cakemate.cake_platform.domain.auth.signin.customer.service;


import com.cakemate.cake_platform.common.command.SearchCommand;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.domain.auth.entity.Customer;
import com.cakemate.cake_platform.domain.auth.exception.EmailNotFoundException;
import com.cakemate.cake_platform.domain.auth.exception.PasswordMismatchException;
import com.cakemate.cake_platform.domain.auth.signin.customer.dto.response.CustomerSignInResponse;
import com.cakemate.cake_platform.domain.auth.signin.customer.dto.response.KakaoTokenResponse;
import com.cakemate.cake_platform.domain.auth.signin.customer.dto.response.KakaoUserResponse;
import com.cakemate.cake_platform.domain.auth.signup.customer.repository.CustomerRepository;
import com.cakemate.cake_platform.domain.member.entity.Member;
import com.cakemate.cake_platform.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class CustomerSignInService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;
    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.client-secret}")
    private String clientSecret;
    @Value("${kakao.kauth-host}")
    private String kauthHost;
    @Value("${kakao.kapi-host}")
    private String kapiHost;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public CustomerSignInService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder,
                                 MemberRepository memberRepository, JwtUtil jwtUtil, ObjectMapper objectMapper,
                                 RestClient.Builder restClientBuilder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.restClient = restClientBuilder.baseUrl(kapiHost).build();
    }

    // ObjectMapper -> 자바 객체를 JSON 문자열로 변환 해주거나 혹은 JSON 문자열을 자바 객체로 변환
    public ApiResponse<CustomerSignInResponse> CustomerSignInProcess(String code) {

        // 인가 코드에서 accessToken 가져오기
        try {
            KakaoTokenResponse tokenResponse = getToken(code);
            String accessToken = tokenResponse.getAccess_token();

            // accessToken을 RequestContextHolder.currentRequestAttributes()에 저장
            if (accessToken != null) {
                saveAccessToken(accessToken);

            }
            log.info("11111111:::{} ", accessToken);

            RestClient.RequestBodySpec get = restClient
                    .method(HttpMethod.valueOf("GET"))
                    .uri(kapiHost + "/v2/user/me")
                    .headers(headers -> headers.setBearerAuth(Objects.requireNonNull(accessToken)));
            log.info("get::: {} ", get);

            String body = get.retrieve().body(String.class);
            log.info("body::: {} ", body);

            // body에 담겨 있는 json을 자바 객체로 가져오기 위해 역직렬화 진행
            KakaoUserResponse kakaoUserResponse = objectMapper.readValue(body, KakaoUserResponse.class);
            String email = kakaoUserResponse.getKakao_account().getEmail();

            Customer customer = customerRepository
                    .findByEmail(email)
                    .orElseThrow(() -> new EmailNotFoundException("고객 이메일이 존재하지 않습니다. 회원가입을 진행하시겠습니까?"));

            Member customerInMember = memberRepository
                    .findByCustomer_Email(email)
                    .orElseThrow(() -> new EmailNotFoundException("고객 이메일이 존재하지 않습니다."));
            String customerJwtToken = jwtUtil.createMemberJwtToken(customerInMember);
            CustomerSignInResponse customerSignInResponse = new CustomerSignInResponse(customerJwtToken);

            // 차후 토큰 생성 예정
            ApiResponse<CustomerSignInResponse> SignInSuccess
                    = ApiResponse
                    .success(HttpStatus.OK, "환영합니다 " + customer.getName() + "님", customerSignInResponse);
            return SignInSuccess;

        } catch (Exception e) {
            throw new RuntimeException(e);

        }

//        Customer customer = customerRepository.findByEmail(email)
//                .orElseThrow(() -> new EmailNotFoundException("고객 이메일이 존재하지 않습니다."));
//
//        boolean isMatched = passwordEncoder.matches(password, customer.getPassword());
//        if (!isMatched) {
//            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
//        }
//        Member customerInMember = memberRepository
//                .findByCustomer_Email(email)
//                .orElseThrow(() -> new EmailNotFoundException("고객 이메일이 존재하지 않습니다."));
//
//        String customerJwtToken = jwtUtil.createMemberJwtToken(customerInMember);
//        CustomerSignInResponse customerSignInResponse = new CustomerSignInResponse(customerJwtToken);
//
//        // 차후 토큰 생성 예정
//        ApiResponse<CustomerSignInResponse> SignInSuccess
//                = ApiResponse
//                .success(HttpStatus.OK, "환영합니다 " + customer.getName() + "님", customerSignInResponse);
//        return SignInSuccess;
    }

    public String createDefaultMessage() {
        return "template_object={\"object_type\":\"text\",\"text\":\"Hello, world!\",\"link\":{\"web_url\":\"https://developers.kakao.com\",\"mobile_web_url\":\"https://developers.kakao.com\"}}";
    }

    private HttpSession getSession() {
        System.out.println("getSession 시작");

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        log.info("RequestContextHolder.currentRequestAttributes::: {} ", RequestContextHolder.currentRequestAttributes());
        log.info("attr::::::: {} ", attr);

        HttpSession session = attr.getRequest().getSession();

        log.info("session::::::: {} ", session);
        System.out.println("getSession 끝");

        return session;
    }

    private void saveAccessToken(String accessToken) {
        log.info("saveAccessToken:::: {} ", accessToken);
        getSession().setAttribute("access_token", accessToken);
    }

    // 세션에서 어세스토큰 조회
    private String getAccessToken() {
        System.out.println("getAccessToken 시작");

        String accessToken = (String) getSession().getAttribute("access_token");

        log.info("accessToken::::: {} ", accessToken);
        System.out.println("getAccessToken 끝");
        return accessToken;
    }

    private void invalidateSession() {
        getSession().invalidate();
    }

    public String call(String method, String urlString, String body) throws Exception {
        System.out.println("call 메서드 시작");
        log.info("method:::::: {} ", method);
        log.info("urlString::::: {} ", urlString);
        log.info("body::::: {} ", body);

        RestClient.RequestBodySpec requestSpec
                = restClient
                .method(HttpMethod.valueOf(method))
                .uri(urlString)
                .headers(headers -> headers.setBearerAuth(getAccessToken()));

        log.info("requestSpec:::: {} ", requestSpec);

        if (body != null) {
            log.info("MediaType.APPLICATION_FORM_URLENCODED::: {} ", MediaType.APPLICATION_FORM_URLENCODED);
            log.info("body:: {} ", body);
            requestSpec.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body);
        }
        try {
            String resultBody = requestSpec.retrieve()
                    .body(String.class);
            log.info("resultBody:::::: {} ", resultBody);
            System.out.println("call 메서드 끝");
            return resultBody;
        } catch (RestClientResponseException e) {
            // 에러 메시지 (응답 바디)
            String errorBody = e.getResponseBodyAsString();
            System.out.println("Error Body: " + errorBody);
            return errorBody;
        }
    }

    public String getAuthUrl(String scope) {
        System.out.println("getAuthUrl 메서드 시작");
        log.info("getAuthUrl scope:::: {} ", scope);
        String uriString = UriComponentsBuilder
                .fromHttpUrl(kauthHost + "/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParamIfPresent("scope", scope != null ? Optional.of(scope) : Optional.empty())
                .build()
                .toUriString();
        log.info("uriString:: {} ", uriString);
        System.out.println("getAuthUrl 메서드 끝");
        return uriString;
    }

    public boolean handleAuthorizationCallback(String code) {
        System.out.println("handleAuthorizationCallback 메서드 시작");
        log.info("handleAuthorizationCallback code {} ", code);
        try {
            KakaoTokenResponse tokenResponse = getToken(code);

            log.info("tokenResponse::: {} ", tokenResponse);

            if (tokenResponse != null) {
                log.info("tokenResponse 존재 한다면 saveAccessToken(tokenResponse.getAccess_token());");
                saveAccessToken(tokenResponse.getAccess_token());
                System.out.println("handleAuthorizationCallback 메서드 끝");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private KakaoTokenResponse getToken(String code) throws Exception {
        System.out.println("KakaoTokenResponse getToken 메서드 시작");
        log.info("KakaoTokenResponse getToken code  {} ", code);

        String params = String.format("grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s",
                clientId, clientSecret, code);

        log.info("params::: {} ", params);

        String response = call("POST", kauthHost + "/oauth/token", params);

        log.info("response::: {} ", response);

        KakaoTokenResponse kakaoTokenResponse = objectMapper.readValue(response, KakaoTokenResponse.class);

        log.info("kakaoTokenResponse::::: {} ", kakaoTokenResponse);
        System.out.println("KakaoTokenResponse getToken 메서드 끝");
        return kakaoTokenResponse;
    }

    public ResponseEntity<?> getUserProfile() {
        try {
            String response = call("GET", kapiHost + "/v2/user/me", null);

            log.info("getUserProfile의 response:: {} ", response);

            ResponseEntity<Object> responseEntity = ResponseEntity.ok(objectMapper.readValue(response, Object.class));

            log.info("getUserProfile의 responseEntity:: {} ", responseEntity);

            return responseEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> getFriends() {
        try {
            String response = call("GET", kapiHost + "/v1/api/talk/friends", null);
            return ResponseEntity.ok(objectMapper.readValue(response, Object.class));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> sendMessage(String messageRequest) {
        try {
            String response = call("POST", kapiHost + "/v2/api/talk/memo/default/send", messageRequest);
            return ResponseEntity.ok(objectMapper.readValue(response, Object.class));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> sendMessageToFriend(String uuid, String messageRequest) {
        try {
            String response = call("POST",
                    kapiHost + "/v1/api/talk/friends/message/default/send?receiver_uuids=[" + uuid + "]",
                    messageRequest);
            return ResponseEntity.ok(objectMapper.readValue(response, Object.class));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> logout() {
        try {
            String response = call("POST", kapiHost + "/v1/user/logout", null);
            invalidateSession();
            return ResponseEntity.ok(objectMapper.readValue(response, Object.class));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<?> unlink() {
        try {
            String response = call("POST", kapiHost + "/v1/user/unlink", null);
            invalidateSession();
            return ResponseEntity.ok(objectMapper.readValue(response, Object.class));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
//public ApiResponse<CustomerSignInResponse> CustomerSignInProcess(SearchCommand signInRequest) {
//    String email = signInRequest.getEmail();
//    String password = signInRequest.getPassword();
//
//    Customer customer = customerRepository.findByEmail(email)
//            .orElseThrow(() -> new EmailNotFoundException("고객 이메일이 존재하지 않습니다."));
//
//    boolean isMatched = passwordEncoder.matches(password, customer.getPassword());
//    if (!isMatched) {
//        throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
//    }
//    Member customerInMember = memberRepository
//            .findByCustomer_Email(email)
//            .orElseThrow(() -> new EmailNotFoundException("고객 이메일이 존재하지 않습니다."));
//
//    String customerJwtToken = jwtUtil.createMemberJwtToken(customerInMember);
//    CustomerSignInResponse customerSignInResponse = new CustomerSignInResponse(customerJwtToken);
//
//    // 차후 토큰 생성 예정
//    ApiResponse<CustomerSignInResponse> SignInSuccess
//            = ApiResponse
//            .success(HttpStatus.OK, "환영합니다 " + customer.getName() + "님", customerSignInResponse);
//    return SignInSuccess;
//}