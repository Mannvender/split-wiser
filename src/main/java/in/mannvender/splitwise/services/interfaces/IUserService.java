package in.mannvender.splitwise.services.interfaces;

import in.mannvender.splitwise.models.User;

import java.util.List;

public interface IUserService {
    public List<User> getAllUsers();
    public User getUserById(Long userId);
    public User getUserByEmail(String email);
    public User createUser(String name, String email, String password);
    public User updateUser(String name, String email, String password);
    public void deleteUser(Long userId);
}
