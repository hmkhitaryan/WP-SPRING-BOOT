package com.egs.account;

import com.egs.account.model.User;
import com.egs.account.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class WebportalbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebportalbootApplication.class, args);
    }

    @Autowired
    SecurityService securityService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

//    @Bean
//    ApplicationRunner init(UserRepository userRepository, RoleRepository roleRepository, NotificationRepo notificationRepo) {
//        return args -> {
//
////            final User assigneeUser = getReceiver();
////            assigneeUser.setRoles(new HashSet<>(roleRepository.findAll()));
////            final User createdAssigneeUser = userRepository.save(assigneeUser);
////            securityService.autoLogin(createdAssigneeUser.getUsername(), createdAssigneeUser.getPassword());
////            final Notification notificationAssignee = new Notification(createdAssigneeUser, "request for friendship. ");
////            notificationRepo.save(notificationAssignee);
////            System.out.println();
//        };
//    }

    public User getUser() {
        User user = new User();
        user.setUsername("hmkhitaryan");
        user.setFirstName("hayk");
        user.setLastName("mkhitaryan");
        user.setEmail("hayk_84@mail.ru");
        user.setEnabled(true);
        user.setPassword(bCryptPasswordEncoder.encode("a"));
//        user.setPassword("a");
        user.setPasswordConfirm("a");
//        final Set<Role> roles = new HashSet<>();
//        roles.add(new Role(1L, "ROLE_USER"));
        user.setDateRegistered(new Date());

        return user;
    }
}
