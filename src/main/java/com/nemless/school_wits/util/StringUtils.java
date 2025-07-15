package com.nemless.school_wits.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StringUtils {
    public static boolean isEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }
}
