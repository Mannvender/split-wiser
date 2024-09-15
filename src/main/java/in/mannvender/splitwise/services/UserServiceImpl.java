package in.mannvender.splitwise.services;

import in.mannvender.splitwise.models.User;
import in.mannvender.splitwise.repositories.UserRepo;
import in.mannvender.splitwise.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        return userRepo.findById(userId).orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

    @Override
    public User createUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public User updateUser(User user) {
        if (userRepo.existsById(user.getId())) {
            return userRepo.save(user);
        } else {
            return null;
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRepo.deleteUserById(userId);
    }
}
