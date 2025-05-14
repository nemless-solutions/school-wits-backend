package com.nemless.school_wits.util;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.enums.CourseFileType;
import com.nemless.school_wits.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Slf4j
public class FileUtils {
    private static final Tika tika = new Tika();
    private static final Set<String> ALLOWED_VIDEO_TYPES = Set.of(
            "application/pdf",
            "video/mp4",
            "video/x-matroska",
            "video/x-msvideo",
            "video/quicktime",
            "video/webm"
    );
    private static final Set<String> ALLOWED_PDF_TYPES = Set.of(
            "application/pdf"
    );

    public static void validateCourseFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.info("File is null or empty: {}", file);
            throw new BadRequestException(ResponseMessage.INVALID_FILE);
        }

        String mimeType = getMimeType(file);
        if(!ALLOWED_VIDEO_TYPES.contains(mimeType) && !ALLOWED_PDF_TYPES.contains(mimeType)) {
            throw new BadRequestException(ResponseMessage.INVALID_FILE_TYPE);
        }
    }

    public static CourseFileType getFileType(MultipartFile file) {
        String mimeType = getMimeType(file);
        if(ALLOWED_VIDEO_TYPES.contains(mimeType)) {
            return CourseFileType.VIDEO;
        } else if(ALLOWED_PDF_TYPES.contains(mimeType)) {
            return CourseFileType.PDF;
        }
        return null;
    }

    private static String getMimeType(MultipartFile file) {
        try {
            return tika.detect(file.getInputStream());
        } catch (IOException e) {
            log.error("Error while detecting file type: {}", file);
            throw new BadRequestException(ResponseMessage.INVALID_FILE);
        }
    }
}
