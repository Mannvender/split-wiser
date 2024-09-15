package in.mannvender.splitwise.services;

import in.mannvender.splitwise.models.User;

public interface IUserService {
    public User getUserById(Long userId);
    public User createUser(User user);
    public User updateUser(User user);
    public void deleteUser(Long userId);
}
