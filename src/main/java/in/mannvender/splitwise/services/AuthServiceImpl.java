package in.mannvender.splitwise.services;

import in.mannvender.splitwise.models.User;
import in.mannvender.splitwise.repositories.UserRepo;
import in.mannvender.splitwise.services.interfaces.IAuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Qualifier("messageSource")
    @Autowired
    private MessageSource messageSource;

    @Override
    public Pair<User, String> login(String email, String password) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();
        boolean passwordMatched =  bCryptPasswordEncoder.matches(password, user.getPassword());
        if(!passwordMatched) {
            throw new RuntimeException("Password incorrect");
        }

        // generate jwt token
        MacAlgorithm algorithm = Jwts.SIG.HS256;
        SecretKey secretKey = algorithm.key().build();
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());
        long timeInMillis = System.currentTimeMillis();
        claims.put("iat", timeInMillis);
        claims.put("exp", timeInMillis + 1000 * 60 * 60 * 24 * 7); // 7 days
        claims.put("iss", "split_wiser");

        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();


        Pair<User, String> userTokenPair = Pair.of(user, token);

        return userTokenPair;
    }

    @Override
    public User register(String name, String email, String password) {
        Optional<User> existingUser = userRepo.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        return userRepo.save(user);
    }
}
