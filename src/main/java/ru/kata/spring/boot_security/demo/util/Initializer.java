package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.UserServices;

import java.util.*;

@Component
public class Initializer implements ApplicationListener<ContextRefreshedEvent> {

    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserServices userServices;

    @Autowired
    public Initializer( RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                        UserServices userServices) {
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userServices = userServices;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Role guestRole = new Role();
        guestRole.setRole("ROLE_GUEST");
        roleRepository.save(guestRole);

        Role userRole = new Role();
        userRole.setRole("ROLE_USER");
        roleRepository.save(userRole);

        Role adminRole = new Role();
        adminRole.setRole("ROLE_ADMIN");
        roleRepository.save(adminRole);

        Set<Role> guestRoles = new LinkedHashSet<>();
        Collections.addAll(guestRoles, guestRole);

        Set<Role> userRoles = new LinkedHashSet<>();
        Collections.addAll(userRoles, userRole, guestRole);

        Set<Role> adminRoles = new LinkedHashSet<>();
        Collections.addAll(adminRoles, adminRole, userRole, guestRole);

        User guest = new User( "guest", passwordEncoder.encode("guest"), "guest",
                "guest", 20, "guest@mail.ru", guestRoles );
        userServices.createUser( guest );

        User user = new User( "user", passwordEncoder.encode("user"), "user",
                "guest",30, "user@mail.ru", userRoles );
        userServices.createUser( user );

        User admin = new User( "admin", passwordEncoder.encode("admin"), "admin",
                "admin",40, "admin@mail.ru", adminRoles );
        userServices.createUser( admin );
    }
}
