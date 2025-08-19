package com.cakemate.cake_platform.domain.auth.signin.owner.service;

import com.cakemate.cake_platform.common.command.SearchCommand;

import com.cakemate.cake_platform.common.exception.MemberNotFoundException;
import com.cakemate.cake_platform.domain.auth.exception.OAuthAccountAlreadyBoundException;
import com.cakemate.cake_platform.domain.auth.exception.OwnerNotFoundException;
import com.cakemate.cake_platform.common.jwt.util.JwtUtil;
import com.cakemate.cake_platform.common.dto.ApiResponse;
import com.cakemate.cake_platform.domain.auth.OauthKakao.response.KakaoTokenResponse;
import com.cakemate.cake_platform.domain.auth.OauthKakao.response.KakaoUserResponse;
import com.cakemate.cake_platform.domain.auth.entity.Owner;
import com.cakemate.cake_platform.domain.auth.exception.EmailNotFoundException;
import com.cakemate.cake_platform.domain.auth.exception.PasswordMismatchException;
import com.cakemate.cake_platform.domain.auth.oAuthEnum.OAuthProvider;
import com.cakemate.cake_platform.domain.auth.signin.owner.dto.response.OwnerSignInResponse;
import com.cakemate.cake_platform.domain.auth.signup.owner.dto.response.OwnerSignUpResponse;
import com.cakemate.cake_platform.domain.auth.signup.owner.repository.OwnerRepository;
import com.cakemate.cake_platform.domain.member.entity.Member;
import com.cakemate.cake_platform.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
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
@Transactional
@Service
public class OwnerSignInService {
    private final OwnerRepository ownerRepository;
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
    @Value("${kakao.redirect-uri-signin-owners-kakao}")
    private String redirectUriSignInKakao;


    public OwnerSignInService(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder,
                              MemberRepository memberRepository, JwtUtil jwtUtil, ObjectMapper objectMapper,
                              RestClient.Builder restClientBuilder) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.restClient = restClientBuilder.baseUrl(kapiHost).build();
    }

    public ApiResponse<?> ownerKakaoSignInProcess(String code) {
        // 인가 코드에서 accessToken 가져오기
        KakaoUserResponse kakaoUserResponse = retrieveKakaoUser(code);

        Long kakaoUserId = kakaoUserResponse.getId();
        String kakaoEmail = kakaoUserResponse.getKakao_account().getEmail();
        String kakaoName = kakaoUserResponse.getKakao_account().getName();
        String kakaoUserPhoneNumber = kakaoUserResponse.getKakao_account().getPhone_number();
        String replaceKakaoUserPhoneNumber = kakaoUserPhoneNumber.replaceAll("^\\+82\\s?0?10", "010");

        //기존에 가입한 계정(로컬, 어나더소셜) 이 있는가?

        boolean existsOwner = existsOwner(kakaoName, replaceKakaoUserPhoneNumber);
        Owner ownerByProvide;
        if (existsOwner) {
            Optional<Owner> ownerByNameAndPhoneNumber =
                    ownerRepository.findByNameAndPhoneNumber(kakaoName, replaceKakaoUserPhoneNumber);
            if (ownerByNameAndPhoneNumber.isPresent()) {
                Member customerByKaKaoInMember = findOwnerByKaKaoInMember(kakaoName,
                        replaceKakaoUserPhoneNumber,
                        OAuthProvider.KAKAO,
                        kakaoUserId);

                String customerJwtToken = jwtUtil.createMemberJwtToken(customerByKaKaoInMember);
                OwnerSignInResponse ownerSignInResponse = new OwnerSignInResponse(customerJwtToken);

                ApiResponse<OwnerSignInResponse> SignInSuccess
                        = ApiResponse
                        .success(HttpStatus.OK, "환영합니다 " + kakaoName + "님", ownerSignInResponse);
                return SignInSuccess;
            }
        }

        Owner kakaoUserOwnerInfo = new Owner(kakaoEmail, null, null, kakaoName,
                replaceKakaoUserPhoneNumber, OAuthProvider.KAKAO, kakaoUserId);
        ownerByProvide = ownerRepository.save(kakaoUserOwnerInfo);

        Member custmerMember = new Member(ownerByProvide);
        memberRepository.save(custmerMember);


        OwnerSignUpResponse ownerKakaoSignUpResponse = new OwnerSignUpResponse(ownerByProvide);

        ApiResponse<OwnerSignUpResponse> success = ApiResponse
                .success(HttpStatus.CREATED,
                        ownerByProvide.getName() + "님 회원가입이 완료되었습니다.",
                        ownerKakaoSignUpResponse);
        return success;

    }

    public ApiResponse<OwnerSignInResponse> ownerSignInProcess(SearchCommand signInRequest) {
        String ownerEmail = signInRequest.getEmail();
        String ownerPassword = signInRequest.getPassword();

        Owner owner = ownerRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new EmailNotFoundException("점주 이메일이 존재하지 않습니다."));


        boolean isMatched = passwordEncoder.matches(ownerPassword, owner.getPassword());

        if (!isMatched) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }

        Member ownerInMember = memberRepository
                .findByOwner_Email(ownerEmail)
                .orElseThrow(() -> new EmailNotFoundException("점주 이메일이 존재하지 않습니다."));

        String ownerJwtToken = jwtUtil.createMemberJwtToken(ownerInMember);

        OwnerSignInResponse ownerSignInResponse = new OwnerSignInResponse(ownerJwtToken);

        ApiResponse<OwnerSignInResponse> SignInSuccess
                = ApiResponse.success(HttpStatus.OK, "환영합니다 " + owner.getName() + "님", ownerSignInResponse);
        return SignInSuccess;
    }

    private Member findOwnerByKaKaoInMember(String name, String phonNumber, OAuthProvider provider, Long id) {
        return memberRepository.findOwnerByKaKao(name, phonNumber,
                provider, id).orElseThrow(() -> new MemberNotFoundException("해당 회원을 찾을 수 없습니다."));
    }

    private KakaoUserResponse retrieveKakaoUser(String code) {
        try {
            KakaoTokenResponse tokenResponse = getToken(code);
            String accessToken = tokenResponse.getAccess_token();

            // accessToken을 RequestContextHolder.currentRequestAttributes()에 저장
            if (accessToken != null) {
                saveAccessToken(accessToken);
            }

            RestClient.RequestBodySpec userProfileRequestSpec = restClient
                    .method(HttpMethod.valueOf("GET"))
                    .uri(kapiHost + "/v2/user/me")
                    .headers(headers -> headers.setBearerAuth(Objects.requireNonNull(accessToken)));

            String body = userProfileRequestSpec.retrieve().body(String.class);

            // body에 담겨 있는 json을 자바 객체로 가져오기 위해 역직렬화 진행
            KakaoUserResponse kakaoUserInfo = objectMapper.readValue(body, KakaoUserResponse.class);
            return kakaoUserInfo;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean existsOwner(String name, String phoneNumber) {
        return ownerRepository.existsByNameAndPhoneNumber(name, phoneNumber);
    }

    public String createDefaultMessage() {
        return "template_object={\"object_type\":\"text\",\"text\":\"Hello, world!\",\"link\":{\"web_url\":\"https://developers.kakao.com\",\"mobile_web_url\":\"https://developers.kakao.com\"}}";
    }

    private HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession();
        return session;
    }

    private void saveAccessToken(String accessToken) {
        getSession().setAttribute("access_token", accessToken);
    }

    private void saveIdToken(String IdToken) {
        getSession().setAttribute("Id_token", IdToken);
    }

    // 세션에서 어세스토큰 조회
    private String getAccessToken() {
        String accessToken = (String) getSession().getAttribute("access_token");
        return accessToken;
    }

    private void invalidateSession() {
        getSession().invalidate();
    }

    public String call(String method, String urlString, String body) {
        RestClient.RequestBodySpec requestSpec
                = restClient
                .method(HttpMethod.valueOf(method))
                .uri(urlString)
                .headers(headers -> headers.setBearerAuth(getAccessToken()));
        if (body != null) {
            requestSpec.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body);
        }
        try {
            String resultBody = requestSpec.retrieve()
                    .body(String.class);
            return resultBody;
        } catch (RestClientResponseException e) {
            // 에러 메시지 (응답 바디)
            String errorBody = e.getResponseBodyAsString();
            return errorBody;
        }
    }

    public String getAuthUrlKakao(String scope) {
        String uriString = UriComponentsBuilder
                .fromHttpUrl(kauthHost + "/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUriSignInKakao)
                .queryParam("response_type", "code")
                .queryParamIfPresent("scope", scope != null ? Optional.of(scope) : Optional.empty())
                .build()
                .toUriString();
        return uriString;
    }

    public boolean handleAuthorizationCallback(String code) {
        try {
            KakaoTokenResponse tokenResponse = getToken(code);

            log.info("tokenResponse::: {} ", tokenResponse);

            if (tokenResponse != null) {
                saveAccessToken(tokenResponse.getAccess_token());
                saveIdToken(tokenResponse.getId_token());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private KakaoTokenResponse getToken(String code) throws Exception {
        String params = String.format("grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s",
                clientId, clientSecret, code);
        String response = call("POST", kauthHost + "/oauth/token", params);
        KakaoTokenResponse kakaoTokenResponse = objectMapper.readValue(response, KakaoTokenResponse.class);
        return kakaoTokenResponse;
    }

    public ResponseEntity<?> getUserProfile() {
        try {
            String response = call("GET", kapiHost + "/v2/user/me", null);
            ResponseEntity<Object> responseEntity = ResponseEntity.ok(objectMapper.readValue(response, Object.class));
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
