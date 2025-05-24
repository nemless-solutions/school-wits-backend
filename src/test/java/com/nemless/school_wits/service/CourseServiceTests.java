package com.nemless.school_wits.service;

import com.nemless.school_wits.model.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class CourseServiceTests {
    @Autowired
    private CourseService courseService;

    @Test
    void shouldReturnCourseList() {
        List<Course> result = courseService.getAllCourses();

        assertThat(result.isEmpty()).isFalse();
    }
}
