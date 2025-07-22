package com.nemless.school_wits.util;

import com.nemless.school_wits.config.ResponseMessage;
import com.nemless.school_wits.enums.CourseFileType;
import com.nemless.school_wits.exception.BadRequestException;
import com.nemless.school_wits.exception.InternalServerException;
import com.nemless.school_wits.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
public class FileUtils {
    private static final Tika tika = new Tika();
    private static final Set<String> ALLOWED_VIDEO_TYPES = Set.of(
            "video/mp4",
            "video/x-matroska",
            "video/x-msvideo",
            "video/quicktime",
            "video/webm"
    );
    private static final Set<String> ALLOWED_PDF_TYPES = Set.of(
            "application/pdf"
    );

    private static String directory;

    @Value("${upload-directory}")
    public void setDirectory(String dir) {
        directory = dir;
    }

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

    public static String saveFile(MultipartFile file, String path) {
        File uploadDir = new File(directory + path);
        if (!uploadDir.exists()) {
            try {
                Files.createDirectory(uploadDir.toPath());
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new InternalServerException(ResponseMessage.UNABLE_TO_UPLOAD);
            }
        }

        String fileName = generateUid() + "." + getFileExtension(file);
        Path filePath = Paths.get(directory, path, fileName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InternalServerException(ResponseMessage.UNABLE_TO_UPLOAD);
        }

        return fileName;
    }

    private static String generateUid() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    private static String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        }
        return extension;
    }

    public static ResponseEntity<Resource> downloadFile(String filePath, String fileName) {
        Path file = Paths.get(directory, filePath, fileName).toAbsolutePath().normalize();
        if (!Files.exists(file)) {
            log.error("File not found: {}", file);
            throw new ResourceNotFoundException(ResponseMessage.UNABLE_TO_DOWNLOAD);
        }

        Resource resource;
        try {
            resource = new UrlResource(file.toUri());
        } catch (MalformedURLException e) {
            log.error("Malformed URL: {}", e.getMessage());
            throw new ResourceNotFoundException(ResponseMessage.UNABLE_TO_DOWNLOAD);
        }
        if (!resource.exists() || !resource.isReadable()) {
            log.error("Resource is not readable: {}", resource);
            throw new ResourceNotFoundException(ResponseMessage.UNABLE_TO_DOWNLOAD);
        }

        String contentType;
        try {
            contentType = Files.probeContentType(file);
        } catch (IOException e) {
            log.warn("Could not determine file content type: {}", file);
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    public static ResponseEntity<Resource> streamFile(String filePath, String fileName, String rangeHeader) {
        Path videoPath = Paths.get(directory, filePath, fileName).toAbsolutePath().normalize();
        long fileSize;
        try {
            fileSize = Files.size(videoPath);
        } catch (IOException e) {
            log.error("Can not get file size: {}", e.getMessage());
            throw new ResourceNotFoundException(ResponseMessage.UNABLE_TO_STREAM);
        }

        UrlResource videoResource;
        try {
            videoResource = new UrlResource(videoPath.toUri());
        } catch (MalformedURLException e) {
            log.error("MalformedURLException: {}", e.getMessage());
            throw new ResourceNotFoundException(ResponseMessage.UNABLE_TO_STREAM);
        }
        if (!videoResource.exists() || !videoResource.isReadable()) {
            throw new ResourceNotFoundException(ResponseMessage.UNABLE_TO_STREAM);
        }

        long start = 0;
        long end = fileSize - 1;

        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String[] ranges = rangeHeader.replace("bytes=", "").split("-");
            start = Long.parseLong(ranges[0]);
            if (ranges.length > 1 && !ranges[1].isEmpty()) {
                end = Long.parseLong(ranges[1]);
            }
        }

        if (start > end) {
            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                    .header(HttpHeaders.CONTENT_RANGE, "bytes */" + fileSize)
                    .build();
        }

        long contentLength = end - start + 1;
        InputStream inputStream;
        try {
            inputStream = Files.newInputStream(videoPath);
        } catch (IOException e) {
            log.error("Can not build input stream: {}", e.getMessage());
            throw new ResourceNotFoundException(ResponseMessage.UNABLE_TO_STREAM);
        }

        try {
            inputStream.skip(start);
        } catch (IOException e) {
            log.error("Can not skip stream: {}", e.getMessage());
            throw new ResourceNotFoundException(ResponseMessage.UNABLE_TO_STREAM);
        }

        InputStreamResource resource = new InputStreamResource(new LimitedInputStream(inputStream, contentLength));

        try {
            return ResponseEntity.status(rangeHeader != null ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(videoPath))
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
                    .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize)
                    .body(resource);
        } catch (IOException e) {
            log.error("Can not return stream response: {}", e.getMessage());
            throw new ResourceNotFoundException(ResponseMessage.UNABLE_TO_STREAM);
        }

    }


    public static void deleteFile(String filePath, String fileName) {
        Path file = Paths.get(directory, filePath, fileName).toAbsolutePath().normalize();

        if (Files.exists(file)) {
            try {
                Files.delete(file);
            } catch (IOException e) {
                log.error("Error deleting file: {}", file, e);
                throw new ResourceNotFoundException(ResponseMessage.UNABLE_TO_DELETE);
            }
            log.info("File deleted successfully: {}", file);
        } else {
            log.warn("File does not exist: {}", file);
            throw new ResourceNotFoundException(ResponseMessage.UNABLE_TO_DELETE);
        }
    }
}
