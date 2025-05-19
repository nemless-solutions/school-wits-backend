package com.nemless.school_wits.config;

import com.nemless.school_wits.enums.Curriculum;
import com.nemless.school_wits.enums.Grade;
import com.nemless.school_wits.model.Role;
import com.nemless.school_wits.model.User;
import com.nemless.school_wits.repository.RoleRepository;
import com.nemless.school_wits.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void run(String... args) {
        if (roleRepository.findAll().isEmpty()) {
            Role roleUser = new Role();
            roleUser.setName(com.nemless.school_wits.enums.Role.ROLE_STUDENT);
            roleRepository.save(roleUser);

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
            LocalDate date = LocalDate.of(1998, 1, 12);
            Date dob = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            user.setDateOfBirth(dob);

            List<Role> roles = roleRepository.findByNameIn(
                    List.of(com.nemless.school_wits.enums.Role.ROLE_STUDENT)
            );
            user.setRoles(roles);

            userRepository.save(user);
        }
    }
}
