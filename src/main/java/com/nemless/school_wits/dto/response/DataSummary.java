package com.nemless.school_wits.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DataSummary {
    private long numberOfStudents;
    private long numberOfTeachers;
    private long numberOfUploadedContent;
}
