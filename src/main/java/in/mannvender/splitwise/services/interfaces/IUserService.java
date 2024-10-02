package in.mannvender.splitwise.services.interfaces;

import in.mannvender.splitwise.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    public List<User> getAllUsers();
    public List<User> getUsersByIds(List<Long> userIds);
    public Optional<User> getUserById(Long userId);
    public Optional<User> getUserByEmail(String email);
    public User createUser(String name, String email, String password);
    public User updateUser(String name, String email, String password);
    public void deleteUser(Long userId);
}
