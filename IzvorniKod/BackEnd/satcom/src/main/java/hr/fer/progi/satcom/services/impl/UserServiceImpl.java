package hr.fer.progi.satcom.services.impl;

import hr.fer.progi.satcom.dao.UserRepository;
import hr.fer.progi.satcom.models.User;
import hr.fer.progi.satcom.services.UserService;
import hr.fer.progi.satcom.services.exceptions.EntityMissingException;
import hr.fer.progi.satcom.services.exceptions.RequestDeniedException;
import hr.fer.progi.satcom.utils.Role;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> listAll() {
        return userRepo.findAll();
    }

    @Override
    public User fetch(Long userId) {
        return findById(userId).orElseThrow(
                () -> new EntityMissingException(User.class, userId)
        );
    }

    @Override
    public Optional<User> findById(Long userId) {
        Assert.notNull(userId, "UserId must be given");
        return userRepo.findById(userId);
    }


    @Override
    public User createNewUser(User user) {
        validate(user);

        if(userRepo.existsByUsername(user.getUsername()))
            throw new RequestDeniedException("Username is already taken");
        if(userRepo.existsByEmail(user.getEmail()))
            throw new RequestDeniedException("Email is already in use");

        if(Arrays.stream(Role.values()).noneMatch(role -> role.toString().equals(user.getRole())))
            throw new RequestDeniedException("No such role defined");
        return userRepo.save(user);

    }

    @Override
    public User updateUser(User user) {
        return userRepo.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    @Override
    public User deleteUser(Long userId) {
        User user = fetch(userId);
        userRepo.delete(user);
        return user;
    }

    private void validate(User user) {
        Assert.isNull(user.getUserId(),
                "User ID must be null, not: " + user.getUserId()
        );
        Assert.notNull(user, "User object must be given");
        Assert.notNull(user.getEmail(), "Email must be given");
        Assert.notNull(user.getUsername(), "Username must be given");
        Assert.notNull(user.getRole(), "Role must be given");
        Assert.notNull(user.getPassword(), "Password must be given");
    }
}
