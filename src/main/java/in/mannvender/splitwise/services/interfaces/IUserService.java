package in.mannvender.splitwise.services.interfaces;

import in.mannvender.splitwise.models.User;

import java.util.List;

public interface IUserService {
    public List<User> getAllUsers();
    public User getUserById(Long userId);
    public User getUserByEmail(String email);
    public User createUser(User user);
    public User updateUser(User user);
    public void deleteUser(Long userId);
}
