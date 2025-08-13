package com.cakemate.cake_platform.domain.auth.oAuthEnum;

import com.cakemate.cake_platform.domain.proposalForm.enums.ProposalFormStatus;
import lombok.Getter;

@Getter
public enum OAuthProvider {
    LOCAL("CakeMate"),
    KAKAO("카카오"),
    NAVER("네이버"),
    GOOGLE("구글");
    private final String oAuthName;

    OAuthProvider(String oAuthName) {
        this.oAuthName = oAuthName;
    }
    public static OAuthProvider fromString(String str) {
        for (OAuthProvider p : values()) {
            if (p.oAuthName.equalsIgnoreCase(str)) {
                return p;
            }
        }
        // 일치하는 enum 이 없을 때 예외발생
        throw new IllegalArgumentException("일치하는 OAuthProvider의 Enum이 없습니다 " + str);
    }
}
