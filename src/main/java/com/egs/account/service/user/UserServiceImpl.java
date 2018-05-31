package com.egs.account.service.user;

import com.egs.account.model.User;
import com.egs.account.model.verification.VerificationToken;
import com.egs.account.repository.role.RoleRepository;
import com.egs.account.repository.user.UserRepository;
import com.egs.account.repository.verificationToken.VerificationTokenRepository;
import com.egs.account.utils.domainUtils.DomainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DomainUtils domainUtils;

    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roleRepository.findAll()));
        user.setDateRegistered(new Date());
        LOGGER.info("user with username {} successfully saved", user.getUsername());

        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        User user = userRepository.getOne(id);
        domainUtils.handleNotFoundError(user, User.class, id);

        return user;
    }

    @Override
    public void updateUser(User user) {
        Long id = user.getId();
        final User entity = userRepository.getOne(id);
        domainUtils.handleNotFoundError(entity, User.class, id);
        updateUserInfo(entity, user);
        userRepository.save(entity);
        LOGGER.info("user with id {} successfully updated", user.getId());
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
        LOGGER.info("user with id {} successfully deleted", id);
    }

    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken vToken = new VerificationToken(token, user);
        tokenRepository.save(vToken);
    }

    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }
        //TODO separate the logic due to single responsibility principal
        user.setEnabled(true);
        userRepository.save(user);

        return TOKEN_VALID;
    }

    /**
     * Update user info.
     *
     * @param u1 user whose info will be updated
     * @param u2 whose info will be used
     */
    private void updateUserInfo(User u1, User u2) {
        u1.setFirstName(u2.getFirstName());
        u1.setLastName(u2.getLastName());
        u1.setEmail(u2.getEmail());
        u1.setSkypeID(u2.getSkypeID());
        u1.setDateRegistered(new Date());
        u1.setPassword(bCryptPasswordEncoder.encode(u2.getPassword()));
    }
}
