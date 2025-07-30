package com.nemless.school_wits.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class UpdateBundleFeesDto {
    private float fee;
    private float discountedFee;
}
