package com.nemless.school_wits.config;

import com.nemless.school_wits.enums.Curriculum;
import com.nemless.school_wits.enums.Grade;
import com.nemless.school_wits.model.Course;
import com.nemless.school_wits.model.Role;
import com.nemless.school_wits.model.User;
import com.nemless.school_wits.repository.CourseRepository;
import com.nemless.school_wits.repository.RoleRepository;
import com.nemless.school_wits.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) {
        if (roleRepository.findAll().isEmpty()) {
            Role roleUser = new Role();
            roleUser.setName(com.nemless.school_wits.enums.Role.ROLE_STUDENT);
            Role roleAdmin = new Role();
            roleAdmin.setName(com.nemless.school_wits.enums.Role.ROLE_ADMIN);
            Role roleTeacher = new Role();
            roleTeacher.setName(com.nemless.school_wits.enums.Role.ROLE_TEACHER);
            roleRepository.saveAll(List.of(roleUser, roleAdmin, roleTeacher));

            User admin = new User();
            admin.setEmail("admin@schoolwits.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setFullName("Admin Ahmed");
            admin.setContact("01722797614");
            admin.setUid("10234567");
            admin.setFatherName("Mr Father");
            admin.setMotherName("Mrs Mother");
            admin.setGuardianEmail("guardian@mail.com");
            admin.setGuardianContact("01867369765");
            admin.setCurriculum(Curriculum.CAMBRIDGE);
            admin.setGrade(Grade.X);
            LocalDate date = LocalDate.of(1998, 1, 12);
            Date dob = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            admin.setDateOfBirth(dob);
            List<Role> roles = roleRepository.findByNameIn(
                    List.of(com.nemless.school_wits.enums.Role.ROLE_ADMIN, com.nemless.school_wits.enums.Role.ROLE_STUDENT)
            );
            admin.setRoles(roles);

            User user = new User();
            user.setEmail("fayeazahmed@schoolwits.com");
            user.setPassword(passwordEncoder.encode("adminadmin"));
            user.setFullName("Fayeaz Ahmed");
            user.setContact("01722797614");
            user.setUid("01234567");
            user.setFatherName("Mr Father");
            user.setMotherName("Mrs Mother");
            user.setGuardianEmail("guardian@mail.com");
            user.setGuardianContact("01867369765");
            user.setCurriculum(Curriculum.CAMBRIDGE);
            user.setGrade(Grade.X);
            LocalDate date1 = LocalDate.of(1998, 1, 12);
            Date dob1 = Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant());
            user.setDateOfBirth(dob1);

            List<Role> roles1 = roleRepository.findByNameIn(
                    List.of(com.nemless.school_wits.enums.Role.ROLE_STUDENT)
            );
            user.setRoles(roles1);

            userRepository.saveAll(List.of(admin, user));

            List<String> titles = new ArrayList<>(
                    List.of(
                            "Physics",
                            "Chemistry",
                            "Biology",
                            "Mathematics",
                            "Advanced Mathematics",
                            "Programming with Python"
                    )
            );
            List<Course> courses = new ArrayList<>();

            for(Grade grade : Grade.values()) {
                for(String title : titles) {
                    Course course = Course.builder()
                            .uid(title + "-" + grade)
                            .title(title)
                            .grade(grade)
                            .description("Dummy description")
                            .fee(100)
                            .build();
                    courses.add(course);
                }
            }
            courseRepository.saveAll(courses);
        }
    }
}
