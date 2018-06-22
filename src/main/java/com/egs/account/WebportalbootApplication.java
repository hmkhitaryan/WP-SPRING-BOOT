package com.egs.account;

import com.egs.account.model.Notification;
import com.egs.account.model.Role;
import com.egs.account.model.User;
import com.egs.account.repository.notification.NotificationRepo;
import com.egs.account.repository.role.RoleRepository;
import com.egs.account.repository.user.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class WebportalbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebportalbootApplication.class, args);
    }


    @Bean
    ApplicationRunner init(UserRepository userRepository, RoleRepository roleRepository, NotificationRepo notificationRepo) {
        return args -> {

            final User assigneeUser = getUser();
            final User createdAssigneeUser = userRepository.save(assigneeUser);
            final Notification notificationAssignee = new Notification(createdAssigneeUser, "request for friendship. ");
            notificationRepo.save(notificationAssignee);
            System.out.println();
        };
    }

    public User getUser() {
        User user = new User();
        user.setUsername("hmkhitaryan");
        user.setFirstName("hayk");
        user.setLastName("mkhitaryan");
        user.setEmail("hayk_84@mail.ru");
        user.setEnabled(true);
        user.setPassword("a");
        user.setPasswordConfirm("a");
        final Set<Role> roles = new HashSet<>();
        roles.add(new Role(1L, "ROLE_USER"));
        user.setRoles(roles);
        user.setDateRegistered(new Date());

        return user;
    }
}
