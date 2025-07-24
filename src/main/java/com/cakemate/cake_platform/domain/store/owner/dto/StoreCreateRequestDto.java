package com.cakemate.cake_platform.domain.store.owner.dto;

import com.cakemate.cake_platform.domain.store.entity.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StoreCreateRequestDto {

    @NotBlank(message = "가게 이름은 필수입니다.")
    private String name;
    @NotBlank(message = "상호명은 필수입니다.")
    private String businessName;
    @NotBlank(message = "가게 주소는 필수입니다.")
    private String address;
    @NotBlank(message = "사업자 등록번호는 필수입니다.")
    private String businessNumber;
    @NotBlank(message = "가게 전화번호는 필수입니다.")
    private String phoneNumber;

    private String image;

    //기본생성자
    public StoreCreateRequestDto() {

    }


}
