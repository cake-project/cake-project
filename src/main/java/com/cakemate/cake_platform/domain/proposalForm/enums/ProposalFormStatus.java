package com.cakemate.cake_platform.domain.proposalForm.enums;


public enum ProposalFormStatus {
    AWAITING("AWAITING", "컨택 대기 중"),
    ACCEPTED("ACCEPTED", "컨택 완료"),
    CONFIRMED("CONFIRMED", "점주 최종 확정"),
    CANCELLED("CANCELLED", "컨택 실패");
    private final String strValue;
    private final String description;

    ProposalFormStatus(String strValue, String description) {
        this.strValue = strValue;
        this.description = description;
    }

    // Enum 대소문자 구분 없이 사용하기 위해 사용하는 메서드
    public static ProposalFormStatus fromString(String str) {
        for (ProposalFormStatus p : values()) {
            if (p.strValue.equalsIgnoreCase(str)) {
                return p;
            }
        }
        // 일치하는 enum 이 없을 때 예외발생
        throw new IllegalArgumentException("일치하는 ProposalFormStatus의 Enum이 없습니다 " + str);
    }
}
