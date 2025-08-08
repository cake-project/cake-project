//package com.cakemate.cake_platform.domain.auth.signin.customer.service;
//
//import com.cakemate.cake_platform.domain.auth.signin.customer.dto.response.KakaoTokenResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpSession;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestClient;
//import org.springframework.web.client.RestClientResponseException;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.util.Optional;
//
//@Service
//@Slf4j
//public class KakaoSignInService {
//    private final ObjectMapper objectMapper;
//    private final RestClient restClient;
//    @Value("${kakao.client-id}")
//    private String clientId;
//    @Value("${kakao.client-secret}")
//    private String clientSecret;
//    @Value("${kakao.kauth-host}")
//    private String kauthHost;
//    @Value("${kakao.kapi-host}")
//    private String kapiHost;
//    @Value("${kakao.redirect-uri}")
//    private String redirectUri;
//
//    public KakaoSignInService(ObjectMapper objectMapper, RestClient.Builder restClientBuilder) {
//        this.objectMapper = objectMapper;
//        this.restClient = restClientBuilder.baseUrl(kapiHost).build();
//    }
//
//    public String createDefaultMessage() {
//        return "template_object={\"object_type\":\"text\",\"text\":\"Hello, world!\",\"link\":{\"web_url\":\"https://developers.kakao.com\",\"mobile_web_url\":\"https://developers.kakao.com\"}}";
//    }
//    private HttpSession getSession() {
//        System.out.println("getSession 시작");
//        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        log.info("RequestContextHolder.currentRequestAttributes::: {} ", RequestContextHolder.currentRequestAttributes());
//        log.info("attr::::::: {} ", attr);
//        HttpSession session = attr.getRequest().getSession();
//        log.info("session::::::: {} ", session);
//        System.out.println("getSession 끝");
//        return session;
//    }
//
//    private void saveAccessToken(String accessToken) {
//        log.info("saveAccessToken:::: {} ", accessToken);
//        getSession().setAttribute("access_token", accessToken);
//    }
//
//    public String getAccessToken() {
//        System.out.println("getAccessToken 시작");
//        String accessToken = (String) getSession().getAttribute("access_token");
//        log.info("accessToken::::: {} ", accessToken);
//        System.out.println("getAccessToken 끝");
//        return accessToken;
//    }
//
//    private void invalidateSession() {
//        getSession().invalidate();
//    }
//
//    public String call(String method, String urlString, String body) throws Exception {
//        System.out.println("call 메서드 시작");
//        log.info("method:::::: {} ", method);
//        log.info("urlString::::: {} ",urlString);
//        log.info("body::::: {} ",body);
//
//        RestClient.RequestBodySpec requestSpec
//                = restClient
//                .method(HttpMethod.valueOf(method))
//                .uri(urlString)
//                .headers(headers -> headers.setBearerAuth(getAccessToken()));
//
//        log.info("requestSpec:::: {} ", requestSpec);
//
//        if (body != null) {
//            log.info("MediaType.APPLICATION_FORM_URLENCODED::: {} ", MediaType.APPLICATION_FORM_URLENCODED);
//            log.info("body:: {} ", body);
//            requestSpec.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                    .body(body);
//        }
//        try {
//            String resultBody = requestSpec.retrieve()
//                    .body(String.class);
//            log.info("resultBody:::::: {} ", resultBody);
//            System.out.println("call 메서드 끝");
//            return resultBody;
//        } catch (RestClientResponseException e) {
//            // 에러 메시지 (응답 바디)
//            String errorBody = e.getResponseBodyAsString();
//            System.out.println("Error Body: " + errorBody);
//            return errorBody;
//        }
//    }
//
//    public String getAuthUrl(String scope) {
//        System.out.println("getAuthUrl 메서드 시작");
//        log.info("getAuthUrl scope:::: {} ", scope);
//        String uriString = UriComponentsBuilder
//                .fromHttpUrl(kauthHost + "/oauth/authorize")
//                .queryParam("client_id", clientId)
//                .queryParam("redirect_uri", redirectUri)
//                .queryParam("response_type", "code")
//                .queryParamIfPresent("scope", scope != null ? Optional.of(scope) : Optional.empty())
//                .build()
//                .toUriString();
//        log.info("uriString:: {} ", uriString);
//        System.out.println("getAuthUrl 메서드 끝");
//        return uriString;
//    }
//
//    public boolean handleAuthorizationCallback(String code) {
//        System.out.println("handleAuthorizationCallback 메서드 시작");
//        log.info("handleAuthorizationCallback code {} ", code);
//        try {
//            KakaoTokenResponse tokenResponse = getToken(code);
//            log.info("tokenResponse::: {} ", tokenResponse);
//            if (tokenResponse != null) {
//                log.info("tokenResponse 존재 한다면 saveAccessToken(tokenResponse.getAccess_token());");
//                saveAccessToken(tokenResponse.getAccess_token());
//                System.out.println("handleAuthorizationCallback 메서드 끝");
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return false;
//    }
//
//    private KakaoTokenResponse getToken(String code) throws Exception {
//        System.out.println("KakaoTokenResponse getToken 메서드 시작");
//        log.info("KakaoTokenResponse getToken code  {} ", code);
//        String params = String.format("grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s",
//                clientId, clientSecret, code);
//        log.info("params::: {} ", params);
//        String response = call("POST", kauthHost + "/oauth/token", params);
//        log.info("response::: {} ", response);
//        KakaoTokenResponse kakaoTokenResponse = objectMapper.readValue(response, KakaoTokenResponse.class);
//        log.info("kakaoTokenResponse::::: {} ", kakaoTokenResponse);
//        System.out.println("KakaoTokenResponse getToken 메서드 끝");
//        return kakaoTokenResponse;
//    }
//
//    public ResponseEntity<?> getUserProfile() {
//        try {
//            String response = call("GET", kapiHost + "/v2/user/me", null);
//            log.info("getUserProfile의 response:: {} ", response);
//            ResponseEntity<Object> responseEntity = ResponseEntity.ok(objectMapper.readValue(response, Object.class));
//            log.info("getUserProfile의 responseEntity:: {} ", responseEntity);
//            return responseEntity;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().build();
//        }
//    }
//
//    public ResponseEntity<?> getFriends() {
//        try {
//            String response = call("GET", kapiHost + "/v1/api/talk/friends", null);
//            return ResponseEntity.ok(objectMapper.readValue(response, Object.class));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().build();
//        }
//    }
//
//    public ResponseEntity<?> sendMessage(String messageRequest) {
//        try {
//            String response = call("POST", kapiHost + "/v2/api/talk/memo/default/send", messageRequest);
//            return ResponseEntity.ok(objectMapper.readValue(response, Object.class));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().build();
//        }
//    }
//
//    public ResponseEntity<?> sendMessageToFriend(String uuid, String messageRequest) {
//        try {
//            String response = call("POST",
//                    kapiHost + "/v1/api/talk/friends/message/default/send?receiver_uuids=[" + uuid + "]",
//                    messageRequest);
//            return ResponseEntity.ok(objectMapper.readValue(response, Object.class));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().build();
//        }
//    }
//
//    public ResponseEntity<?> logout() {
//        try {
//            String response = call("POST", kapiHost + "/v1/user/logout", null);
//            invalidateSession();
//            return ResponseEntity.ok(objectMapper.readValue(response, Object.class));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().build();
//        }
//    }
//
//    public ResponseEntity<?> unlink() {
//        try {
//            String response = call("POST", kapiHost + "/v1/user/unlink", null);
//            invalidateSession();
//            return ResponseEntity.ok(objectMapper.readValue(response, Object.class));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().build();
//        }
//    }
//}
