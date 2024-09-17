package in.mannvender.splitwise.services.interfaces;

import in.mannvender.splitwise.models.User;
import org.springframework.data.util.Pair;

public interface IAuthService {
    public Pair<User, String> login (String email, String password);
    public User register (String name, String email, String password);
}
