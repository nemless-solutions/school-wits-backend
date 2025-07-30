package com.nemless.school_wits.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class StringUtils {
    public static boolean isEmpty(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static Map<String, List<String>> parseCoreLearningAreaContentString(String content) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        String[] lines = content.split("##");

        for (String section : lines) {
            if (section.trim().isEmpty()) continue;

            String[] parts = section.trim().split("•", 2);
            if (parts.length < 2) continue;

            String header = parts[0].trim();
            String bulletSection = parts[1];

            String[] bullets = bulletSection.split("•");
            List<String> bulletList = new ArrayList<>();
            for (String bullet : bullets) {
                String trimmed = bullet.trim();
                if (!trimmed.isEmpty()) {
                    bulletList.add(trimmed);
                }
            }

            result.put(header, bulletList);
        }

        return result;
    }
}
