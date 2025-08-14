package com.cakemate.cake_platform.domain.auth.OauthKakao.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserResponse {
    private Long id;
    private String connected_at;
    private String synched_at;
    private Properties properties;
    private Kakao_account kakao_account;

    @Getter
    @NoArgsConstructor
    public static class Properties {
        private String profile_image;
        private String thumbnail_image;

    }

    @Getter
    @NoArgsConstructor
    public static class Kakao_account {
        private boolean profile_image_needs_agreement;
        private Profile profile;
        private boolean name_needs_agreement;
        private String name;
        private boolean has_email;
        private boolean email_needs_agreement;
        private boolean is_email_valid;
        private String email;
        private boolean has_phone_number;
        private boolean phone_number_needs_agreement;
        private String phone_number;
        private boolean has_birthday;
        private boolean birthday_needs_agreement;
        private String birthday;
        private String birthday_type;
        private boolean is_leap_month;

        @Getter
        @NoArgsConstructor
        public static class Profile {
            private String thumbnail_image_url;
            private String profile_image_url;
            private boolean is_default_image;
        }
    }

}


