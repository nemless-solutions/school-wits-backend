package com.nemless.school_wits.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nemless.school_wits.dto.CourseInformationDto;
import com.nemless.school_wits.enums.CourseMode;
import com.nemless.school_wits.enums.CourseType;
import com.nemless.school_wits.enums.Curriculum;
import com.nemless.school_wits.enums.Grade;
import com.nemless.school_wits.model.*;
import com.nemless.school_wits.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseInformationRepository courseInformationRepository;
    private final CoursePlanInformationRepository coursePlanInformationRepository;
    private final CoursePlanWeekInformationRepository coursePlanWeekInformationRepository;
    private final CoursePlanWeekDetailsInformationRepository coursePlanWeekDetailsInformationRepository;
    private final CourseBundleRepository courseBundleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) throws IOException {
        if (roleRepository.findAll().isEmpty()) {
            createRoles();
            createUsers();
            createCourses();
            bundleCourses();
            createCoursePlans();
        }
    }

    private void createRoles() {
        Role roleUser = new Role();
        roleUser.setName(com.nemless.school_wits.enums.Role.ROLE_STUDENT);
        Role roleAdmin = new Role();
        roleAdmin.setName(com.nemless.school_wits.enums.Role.ROLE_ADMIN);
        Role roleTeacher = new Role();
        roleTeacher.setName(com.nemless.school_wits.enums.Role.ROLE_TEACHER);
        roleRepository.saveAll(List.of(roleUser, roleAdmin, roleTeacher));
    }

    private void createUsers() {
        User admin = new User();
        admin.setEmail("admin@schoolwits.com");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setFullName("Mr Admin");
        admin.setCurrentSchool("Some School");
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
                List.of(com.nemless.school_wits.enums.Role.ROLE_ADMIN)
        );
        admin.setRoles(roles);

        User user = new User();
        user.setEmail("student@schoolwits.com");
        user.setPassword(passwordEncoder.encode("student"));
        user.setFullName("Mr Student");
        user.setCurrentSchool("Some School");
        user.setUid("01234567");
        user.setFatherName("Mr Father");
        user.setMotherName("Mrs Mother");
        user.setGuardianEmail("guardian@mail.com");
        user.setGuardianContact("01867369765");
        user.setCurriculum(Curriculum.CAMBRIDGE);
        user.setGrade(Grade.IX);
        LocalDate date1 = LocalDate.of(1998, 1, 12);
        Date dob1 = Date.from(date1.atStartOfDay(ZoneId.systemDefault()).toInstant());
        user.setDateOfBirth(dob1);

        List<Role> roles1 = roleRepository.findByNameIn(
                List.of(com.nemless.school_wits.enums.Role.ROLE_STUDENT)
        );
        user.setRoles(roles1);

        userRepository.saveAll(List.of(admin, user));
    }

    private void createCourses() throws IOException {
        List<Course> courses = new ArrayList<>();

        Map<String, List<String>> gradeCourses = loadCourseList();
        for(Map.Entry<String, List<String>> entry : gradeCourses.entrySet()) {
            String title = entry.getKey();
            List<String> grades = entry.getValue();

            for(String grade : grades) {
                Course courseOnline = Course.builder()
                        .uid(title + "-" + grade + "-" + CourseMode.ONLINE)
                        .title(title)
                        .grade(Grade.valueOf(grade))
                        .mode(CourseMode.ONLINE)
                        .type(CourseType.LONG)
                        .description("Dummy description")
                        .fee(getCourseFee(Grade.valueOf(grade), CourseMode.ONLINE, false))
                        .discountedFee(getCourseFee(Grade.valueOf(grade), CourseMode.ONLINE, true))
                        .numberOfLessons(10)
                        .numberOfNotes(10)
                        .numberOfWorksheet(5)
                        .numberOfQuizzes(5)
                        .numberOfExams(2)
                        .numberOfSession(1)
                        .academicSession("2025-2026")
                        .sessionDuration("15 Jun - 28 Jun, 2025")
                        .build();

                Course courseInPerson = Course.builder()
                        .uid(title + "-" + grade + "-" + CourseMode.IN_PERSON)
                        .title(title)
                        .grade(Grade.valueOf(grade))
                        .mode(CourseMode.IN_PERSON)
                        .type(CourseType.LONG)
                        .description("Dummy description")
                        .fee(getCourseFee(Grade.valueOf(grade), CourseMode.IN_PERSON, false))
                        .discountedFee(getCourseFee(Grade.valueOf(grade), CourseMode.IN_PERSON, true))
                        .numberOfLessons(10)
                        .numberOfNotes(10)
                        .numberOfWorksheet(5)
                        .numberOfQuizzes(5)
                        .numberOfExams(2)
                        .numberOfSession(1)
                        .academicSession("2025-2026")
                        .sessionDuration("15 Jun - 28 Jun, 2025")
                        .build();

                courses.add(courseOnline);
                courses.add(courseInPerson);
            }
        }

        courseRepository.saveAll(courses);
    }

    private long getCourseFee(Grade grade, CourseMode mode, boolean isDiscountedPrice) {
        if(grade == Grade.VIII) {
            if(mode == CourseMode.IN_PERSON) {
                return isDiscountedPrice ? 3500L : 4000L;
            } else {
                return 2500L;
            }
        } else if(grade == Grade.IX || grade == Grade.X) {
            if(mode == CourseMode.IN_PERSON) {
                return isDiscountedPrice ? 4000L : 4500L;
            } else {
                return 2500L;
            }
        } else
            return 0L;
    }

    private void bundleCourses() {
        List<CourseBundle> bundles = new ArrayList<>();
        for(Grade grade : Grade.values()) {
            for(CourseMode mode : CourseMode.values()) {
                if(grade.equals(Grade.VIII) || grade.equals(Grade.IX) || grade.equals(Grade.X)) continue;

                List<Course> courses = courseRepository.findAllByGradeAndMode(grade, mode);
                CourseBundle courseBundle = CourseBundle.builder()
                        .grade(grade)
                        .mode(mode)
                        .fee(getBundleFee(mode, false))
                        .discountedFee(getBundleFee(mode, true))
                        .courses(courses)
                        .build();
                bundles.add(courseBundle);
            }
        }
        courseBundleRepository.saveAll(bundles);
    }

    private long getBundleFee(CourseMode mode, boolean isDiscountedPrice) {
        if(mode == CourseMode.IN_PERSON) {
            return isDiscountedPrice ? 10000L : 12000L;
        } else {
            return 5000L;
        }
    }

    private Map<String, List<String>> loadCourseList() throws IOException {
        Map<String, List<String>> courseMap;

        ClassPathResource resource = new ClassPathResource("initialize-db/courses.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            courseMap = reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(line -> line.split(":", 2))  // split into [title, grades]
                    .collect(Collectors.toMap(
                            parts -> parts[0].trim(),
                            parts -> Arrays.stream(parts[1].split(","))
                                    .map(String::trim)
                                    .collect(Collectors.toList())
                    ));
        }

        return courseMap;
    }

    private void createCoursePlans() throws IOException {
        List<CourseInformationDto> courseInformationList = readCourseInformation();

        for(Grade grade : Grade.values()) {
            String fileName = "initialize-db/course-plans-" + grade + ".txt";
            Map<String, List<Map<String, List<String>>>> coursePlans
                    = readFileContent(fileName);

            for(Map.Entry<String, List<Map<String, List<String>>>> entry : coursePlans.entrySet()) {
                String courseUid = entry.getKey() + "-" + grade + "-" + CourseMode.ONLINE;
                saveCoursePlans(courseUid, courseInformationList, entry);
                courseUid = entry.getKey() + "-" + grade + "-" + CourseMode.IN_PERSON;
                saveCoursePlans(courseUid, courseInformationList, entry);
            }
        }
    }

    private void saveCoursePlans(String courseUid, List<CourseInformationDto> courseInformationList, Map.Entry<String, List<Map<String, List<String>>>> entry) {
        Course course = courseRepository.findByUid(courseUid).orElseThrow();
        CourseInformationDto courseInformationDto = courseInformationList.stream()
                .filter(c -> courseUid.contains(c.getCourse()))
                .findFirst().orElseThrow();
        CourseInformation courseInformation = buildCourseInformation(courseInformationDto, course);
        courseInformation = courseInformationRepository.save(courseInformation);

        CoursePlanInformation coursePlanInformation = CoursePlanInformation.builder()
                .courseInformation(courseInformation)
                .build();
        coursePlanInformation = coursePlanInformationRepository.save(coursePlanInformation);

        List<Map<String, List<String>>> weekDetailsList = entry.getValue();
        for(Map<String, List<String>> weekDetails : weekDetailsList) {
            for(Map.Entry<String, List<String>> weekEntry : weekDetails.entrySet()) {
                String coursePlanWeekInformationText = weekEntry.getKey();
                CoursePlanWeekInformation coursePlanWeekInformation = CoursePlanWeekInformation.builder()
                        .coursePlanInformation(coursePlanInformation)
                        .text(coursePlanWeekInformationText)
                        .build();
                coursePlanWeekInformation = coursePlanWeekInformationRepository.save(coursePlanWeekInformation);

                for(String coursePlanWeekInformationDetailsText : weekEntry.getValue()) {
                    CoursePlanWeekDetailsInformation coursePlanWeekDetailsInformation = CoursePlanWeekDetailsInformation.builder()
                            .coursePlanWeekInformation(coursePlanWeekInformation)
                            .text(coursePlanWeekInformationDetailsText)
                            .build();
                    coursePlanWeekDetailsInformationRepository.save(coursePlanWeekDetailsInformation);
                }
            }
        }
    }

    private Map<String, List<Map<String, List<String>>>> readFileContent(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        Map<String, List<Map<String, List<String>>>> coursePlans = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            Pattern titlePattern = Pattern.compile("#(.*?)#");
            String currentCourse = null;
            List<Map<String, List<String>>> weekGroups = null;
            Map<String, List<String>> currentWeekGroup = null;
            String currentWeekHeader = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                Matcher matcher = titlePattern.matcher(line);
                if (matcher.matches()) {
                    currentCourse = matcher.group(1).trim();
                    weekGroups = new ArrayList<>();
                    coursePlans.put(currentCourse, weekGroups);
                    currentWeekGroup = null;
                    currentWeekHeader = null;
                } else if (line.isEmpty()) {
                    currentWeekGroup = null;
                    currentWeekHeader = null;
                } else if (currentCourse != null) {
                    if (currentWeekGroup == null) {
                        currentWeekHeader = line;
                        currentWeekGroup = new LinkedHashMap<>();
                        currentWeekGroup.put(currentWeekHeader, new ArrayList<>());
                        weekGroups.add(currentWeekGroup);
                    } else {
                        currentWeekGroup.get(currentWeekHeader).add(line);
                    }
                }
            }
        }
        return coursePlans;
    }

    private List<CourseInformationDto> readCourseInformation() throws IOException {
        ClassPathResource resource = new ClassPathResource("initialize-db/course-information.json");
        InputStream inputStream = resource.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(inputStream, new TypeReference<>() {});
    }

    private CourseInformation buildCourseInformation(CourseInformationDto courseInformationDto, Course course) {
        return CourseInformation.builder()
                .course(course)
                .title(courseInformationDto.getTitle())
                .description(courseInformationDto.getDescription())
                .learningContentTitle(courseInformationDto.getLearningContentTitle())
                .learningContentList(String.join(" | ", courseInformationDto.getLearningContentList()))
                .assessment(courseInformationDto.getAssessment())
                .academicPlan(courseInformationDto.getAcademicPlan())
                .chartValues(getRandomChartValues())
                .build();
    }

    private Map<String, Integer> getRandomChartValues() {
        Map<String, Integer> chartValues = new HashMap<>();
        for(int i = 1; i <= 5; i++) {
            String topicName = "Topic " + i;
            Integer topicValue = new Random().nextInt(20) + 1;
            chartValues.put(topicName, topicValue);
        }
        return chartValues;
    }
}
