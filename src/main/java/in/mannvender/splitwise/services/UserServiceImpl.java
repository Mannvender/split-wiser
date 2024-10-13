package in.mannvender.splitwise.services;

import in.mannvender.splitwise.models.User;
import in.mannvender.splitwise.repositories.UserRepo;
import in.mannvender.splitwise.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepo userRepo;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(@Lazy BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public List<User> getUsersByIds(List<Long> userIds) {
        return userRepo.findAllById(userIds);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepo.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User createUser(String name, String email, String password) {
        return null;
    }

    @Override
    public User updateUser(String name, String email, String password) {
        Optional<User> existingUser = userRepo.findByEmail(email);
        if (existingUser.isPresent()) {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            return userRepo.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRepo.deleteUserById(userId);
    }
}
