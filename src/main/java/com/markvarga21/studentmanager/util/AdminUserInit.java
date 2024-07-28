package com.markvarga21.studentmanager.util;

import com.markvarga21.studentmanager.dto.Role;
import com.markvarga21.studentmanager.entity.AppUser;
import com.markvarga21.studentmanager.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * A util class for initiating the administrator user
 * on startup if not already existing.
 */
@Component
@RequiredArgsConstructor
public class AdminUserInit
        implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * The app user repository.
     */
    private final AppUserRepository appUserRepository;

    /**
     * The password encoder.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * The default admin password.
     */
    @Value("${default.admin.password}")
    private String defaultAdminPassword;

    /**
     * This method is used to initiate the admin user.
     *
     * @param event The context refreshed event.
     */
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (this.appUserRepository.findByUsername("admin").isEmpty()) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setEmail("admin@domain.com");
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setPassword(
                    this.passwordEncoder.encode(this.defaultAdminPassword)
            );

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(Role.ADMIN);
            adminRoles.add(Role.USER);
            admin.setRoles(adminRoles);
            this.appUserRepository.save(admin);
        }
    }
}
